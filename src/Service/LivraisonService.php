<?php
namespace App\Service;

use App\Entity\Livraison;
use App\DTO\LivraisonListDto;
use App\Enum\StatutLivraison;
use App\Repository\LivraisonRepository;
use App\Repository\LivreurRepository;
use App\Service\Interface\ILivraisonServiceInterface;
use Doctrine\ORM\EntityManagerInterface;

class LivraisonService implements ILivraisonServiceInterface
{
    public function __construct(
        private EntityManagerInterface $em,
        private LivraisonRepository $livraisonRepository,
        private LivreurRepository $livreurRepository
    ) {}

    public function listerLivraisonsEnAttente(int $page, int $itemsPerPage): array
    {
        $offset = ($page - 1) * $itemsPerPage;
        
        $livraisons = $this->livraisonRepository->findByStatutWithPagination(
            'en_attente',
            $itemsPerPage,
            $offset
        );
        
        $total = $this->livraisonRepository->count(['statut' => 'en_attente']);
        
        return [
            'livraisons' => LivraisonListDto::fromEntities($livraisons),
            'totalPages' => ceil($total / $itemsPerPage),
            'currentPage' => $page,
            'total' => $total
        ];
    }

    public function regrouperParZone(): array
    {
        $livraisons = $this->livraisonRepository->findEnAttenteGroupedByZone();
        
        $groupes = [];
        foreach ($livraisons as $livraison) {
            $commande = $livraison->getCommande();
            if ($commande && $commande->getIdZone()) {
                $idZone = $commande->getIdZone();
                if (!isset($groupes[$idZone])) {
                    $groupes[$idZone] = [];
                }
                $groupes[$idZone][] = LivraisonListDto::fromEntitie($livraison);
            }
        }
        
        return $groupes;
    }

    public function affecterLivreur(int $idLivraison, int $idLivreur): bool
    {
        $livraison = $this->livraisonRepository->find($idLivraison);
        
        if (!$livraison) {
            throw new \Exception("Livraison non trouvée");
        }

        $livreur = $this->livreurRepository->find($idLivreur);
        
        if (!$livreur) {
            throw new \Exception("Livreur non trouvé");
        }

        if (!$livreur->getDisponible()) {
            throw new \Exception("Le livreur n'est pas disponible");
        }

        $livraison->setIdLivreur($idLivreur);
        $livraison->setStatut(StatutLivraison::AFFECTEE);
        $livraison->setDateAffectation(new \DateTime());

        $this->em->flush();

        return true;
    }

    public function changerStatutLivraison(int $id, string $nouveauStatut): bool
    {
        $livraison = $this->livraisonRepository->find($id);
        
        if (!$livraison) {
            throw new \Exception("Livraison non trouvée");
        }

        $livraison->setStatut(StatutLivraison::from($nouveauStatut));

        if ($nouveauStatut === 'livree') {
            $livraison->setDateLivraison(new \DateTime());
        }

        $this->em->flush();

        return true;
    }

    public function obtenirLivraisonsLivreur(int $idLivreur, int $page, int $itemsPerPage): array
    {
        $offset = ($page - 1) * $itemsPerPage;
        
        $livraisons = $this->livraisonRepository->findByLivreurWithPagination(
            $idLivreur,
            $itemsPerPage,
            $offset
        );
        
        $total = $this->livraisonRepository->count(['idLivreur' => $idLivreur]);
        
        return [
            'livraisons' => LivraisonListDto::fromEntities($livraisons),
            'totalPages' => ceil($total / $itemsPerPage),
            'currentPage' => $page,
            'total' => $total
        ];
    }
}