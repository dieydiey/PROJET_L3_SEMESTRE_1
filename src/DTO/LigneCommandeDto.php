<?php
namespace App\DTO;

use App\Entity\LigneCommande;

class LigneCommandeDto
{
    public ?int $id;
    public ?int $idBurger;
    public ?int $idMenu;
    public ?int $idComplement;
    public int $quantite;
    public float $prixUnitaire;
    public float $sousTotal;
    public ?string $nomProduit = null;
    public ?string $image = null; // Ajout de la propriété image

    public static function fromEntitie(LigneCommande $entity): LigneCommandeDto
    {
        $dto = new LigneCommandeDto();
        $dto->id = $entity->getIdLigne();
        $dto->idBurger = $entity->getIdBurger();
        $dto->idMenu = $entity->getIdMenu();
        $dto->idComplement = $entity->getIdComplement();
        $dto->quantite = $entity->getQuantite();
        $dto->prixUnitaire = $entity->getPrixUnitaire();
        $dto->sousTotal = $entity->getSousTotal();

        if ($entity->getBurger()) {
            $dto->nomProduit = $entity->getBurger()->getNom();
            $dto->image = $entity->getBurger()->getImage();
            $dto->typeProduit = 'BURGER';
        } elseif ($entity->getMenu()) {
            $dto->nomProduit = $entity->getMenu()->getNom();
            $dto->image = $entity->getMenu()->getImage();
            $dto->typeProduit = 'MENU';
        } elseif ($entity->getComplement()) {
            $dto->nomProduit = $entity->getComplement()->getNom();
            $dto->image = $entity->getComplement()->getImage();
            $dto->typeProduit = 'COMPLÉMENT'; // C'est ici qu'on gère l'affichage du complément
        }
        
        return $dto;
    }

    public static function fromEntities(array $entities): array
    {
        return array_map(function (LigneCommande $entity) {
            return self::fromEntitie($entity);
        }, $entities);
    }
}