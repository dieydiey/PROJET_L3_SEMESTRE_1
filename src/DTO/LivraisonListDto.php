<?php
namespace App\DTO;

use App\Entity\Livraison;
use DateTimeImmutable;

class LivraisonListDto
{
    public ?int $id;
    public int $idCommande;
    public ?int $idLivreur;
    public string $adresseLivraison;
    public ?DateTimeImmutable $dateAffectation;
    public ?DateTimeImmutable $dateLivraison;
    public string $statut;
    public ?string $numeroCommande = null;
    public ?string $nomLivreur = null;

    public static function fromEntitie(Livraison $entity): LivraisonListDto
    {
        $dto = new LivraisonListDto();
        $dto->id = $entity->getIdLivraison();
        $dto->idCommande = $entity->getIdCommande();
        $dto->idLivreur = $entity->getIdLivreur();
        $dto->adresseLivraison = $entity->getAdresseLivraison();
        $dto->dateAffectation = $entity->getDateAffectation() 
            ? DateTimeImmutable::createFromMutable($entity->getDateAffectation())
            : null;
        $dto->dateLivraison = $entity->getDateLivraison()
            ? DateTimeImmutable::createFromMutable($entity->getDateLivraison())
            : null;
        $dto->statut = $entity->getStatut()->value;
        
        if ($entity->getCommande()) {
            $dto->numeroCommande = $entity->getCommande()->getNumeroCommande();
        }
        
        return $dto;
    }

    public static function fromEntities(array $entities): array
    {
        return array_map(function (Livraison $entity) {
            return self::fromEntitie($entity);
        }, $entities);
    }
}
