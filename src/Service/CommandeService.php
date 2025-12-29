<?php
namespace App\Service;

use App\Entity\Commande;
use App\DTO\FiltreCommandeDto;
use App\DTO\CommandeListDto;
use App\DTO\CommandeDetailDto;
use App\Enum\EtatCommande;
use App\Service\Interface\ICommandeServiceInterface;
use App\Repository\CommandeRepository;
use Doctrine\ORM\EntityManagerInterface;

class CommandeService implements ICommandeServiceInterface
{
    public function __construct(
        private EntityManagerInterface $em,
        private CommandeRepository $commandeRepository
    ) {}

    public function listerCommandes(?FiltreCommandeDto $filtre, int $page, int $itemsPerPage): array
    {
        $offset = ($page - 1) * $itemsPerPage;
        
        $commandes = $this->commandeRepository->findWithFilters($filtre, $itemsPerPage, $offset);
        $total = $this->commandeRepository->countWithFilters($filtre);
        
        return [
            'commandes' => CommandeListDto::fromEntities($commandes),
            'totalPages' => ceil($total / $itemsPerPage),
            'currentPage' => $page,
            'total' => $total
        ];
    }

    public function obtenirCommande(int $id): ?CommandeDetailDto
    {
        $commande = $this->commandeRepository->find($id);
        
        if (!$commande) {
            return null;
        }
        
        return CommandeDetailDto::fromEntitie($commande);
    }

    public function changerEtatCommande(int $id, string $nouvelEtat): bool
    {
        $commande = $this->commandeRepository->find($id);
        
        if (!$commande) {
            throw new \Exception("Commande non trouvée");
        }

        if ($commande->getEtat() === EtatCommande::TERMINEE || $commande->getEtat() === EtatCommande::ANNULEE) {
            throw new \Exception("Impossible de modifier une commande terminée ou annulée");
        }

        $etatEnum = EtatCommande::from($nouvelEtat);
        $commande->setEtat($etatEnum);

        if ($etatEnum === EtatCommande::PRETE && $commande->getModeConsommation() === 'livraison') {
            
            $livraisonExistante = $this->em->getRepository(Livraison::class)->findOneBy(['commande' => $commande]);
            
            if (!$livraisonExistante) {
                $livraison = new Livraison();
                $livraison->setCommande($commande);
                
                $adresse = $commande->getClient() ? $commande->getClient()->getAdresse() : 'Adresse non spécifiée';
                $livraison->setAdresseLivraison($adresse);
                $livraison->setStatut(StatutLivraison::EN_ATTENTE); 
                
                $this->em->persist($livraison);
            }
        }

        $this->em->flush();

        return true;
    }

    public function annulerCommande(int $id): bool
    {
        $commande = $this->commandeRepository->find($id);
        
        if (!$commande) {
            throw new \Exception("Commande non trouvée");
        }

        if ($commande->getEtat() === EtatCommande::TERMINEE) {
            throw new \Exception("Impossible d'annuler une commande terminée");
        }

        $commande->setEtat(EtatCommande::ANNULEE);
        $this->em->flush();

        return true;
    }

    public function obtenirCommandesClient(int $idClient, int $page, int $itemsPerPage): array
    {
        $offset = ($page - 1) * $itemsPerPage;
        
        $commandes = $this->commandeRepository->findBy(
            ['idClient' => $idClient],
            ['dateCommande' => 'DESC'],
            $itemsPerPage,
            $offset
        );
        
        $total = $this->commandeRepository->count(['idClient' => $idClient]);
        
        return [
            'commandes' => CommandeListDto::fromEntities($commandes),
            'totalPages' => ceil($total / $itemsPerPage),
            'currentPage' => $page,
            'total' => $total
        ];
    }
}
