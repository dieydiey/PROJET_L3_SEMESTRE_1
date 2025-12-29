<?php
namespace App\DTO;

use App\Entity\Burger;
use DateTimeImmutable;

class BurgerListDto
{
    public ?int $id;
    public string $nom;
    public float $prix;
    public ?string $image;
    public bool $isArchived;
    public DateTimeImmutable $createAt;

    public static function fromEntitie(Burger $entity): BurgerListDto
    {
        $dto = new BurgerListDto();
        $dto->id = $entity->getIdBurger();
        $dto->nom = $entity->getNom();
        $dto->prix = $entity->getPrix();
        $dto->image = $entity->getImage();
        $dto->isArchived = $entity->getArchive();
        $dto->createAt = DateTimeImmutable::createFromMutable($entity->getDateCreation());
        return $dto;
    }

    public static function fromEntities(array $entities): array
    {
        return array_map(function (Burger $entity) {
            return self::fromEntitie($entity);
        }, $entities);
    }
}
