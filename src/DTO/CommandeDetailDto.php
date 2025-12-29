<?php
namespace App\DTO;

use App\Entity\Commande;
use DateTimeImmutable;

class CommandeDetailDto
{
    public ?int $id;
    public string $numeroCommande;
    public int $idClient;
    public ?string $nomClient = null;
    public DateTimeImmutable $dateCommande;
    public string $modeConsommation;
    public string $etat;
    public float $montantTotal;
    public ?int $idZone;
    public array $lignes = [];
    public ?array $paiement = null;
    public ?array $livraison = null;

    public static function fromEntitie(Commande $entity): CommandeDetailDto
    {
        $dto = new CommandeDetailDto();
        $dto->id = $entity->getIdCommande();
        $dto->numeroCommande = $entity->getNumeroCommande();
        $dto->idClient = $entity->getIdClient();
        $dto->nomClient = $entity->getClient()->getPrenom() . ' ' . $entity->getClient()->getNom();
        $dto->dateCommande = DateTimeImmutable::createFromMutable($entity->getDateCommande());
        $dto->modeConsommation = $entity->getModeConsommation()->value;
        $dto->etat = $entity->getEtat()->value;
        $dto->montantTotal = $entity->getMontantTotal();
        $dto->idZone = $entity->getIdZone();
        
        foreach ($entity->getLignesCommande() as $ligne) {
            $dto->lignes[] = LigneCommandeDto::fromEntitie($ligne);
        }
        
        if ($entity->getPaiement()) {
            $dto->paiement = [
                'id' => $entity->getPaiement()->getIdPaiement(),
                'montant' => $entity->getPaiement()->getMontant(),
                'methode' => $entity->getPaiement()->getMethodePaiement()->value,
                'statut' => $entity->getPaiement()->getStatut()->value,
            ];
        }
        
        if ($entity->getLivraison()) {
            $dto->livraison = [
                'id' => $entity->getLivraison()->getIdLivraison(),
                'idLivreur' => $entity->getLivraison()->getIdLivreur(),
                'adresse' => $entity->getLivraison()->getAdresseLivraison(),
                'statut' => $entity->getLivraison()->getStatut()->value,
            ];
        }
        
        return $dto;
    }
}