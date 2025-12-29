<?php
namespace App\DTO;

use App\Entity\Menu;
use DateTimeImmutable;

class MenuListDto
{
    public ?int $id;
    public string $nom;
    public ?string $image;
    public bool $isArchived;
    public DateTimeImmutable $createAt;
    public int $nbCompositions = 0;

    public static function fromEntitie(Menu $entity): MenuListDto
    {
        $dto = new MenuListDto();
        $dto->id = $entity->getIdMenu();
        $dto->nom = $entity->getNom();
        $dto->image = $entity->getImage();
        $dto->isArchived = $entity->getArchive();
        $dto->createAt = DateTimeImmutable::createFromMutable($entity->getDateCreation());
        $dto->nbCompositions = count($entity->getCompositions()->toArray());
        return $dto;
    }

    public static function fromEntities(array $entities): array
    {
        return array_map(function (Menu $entity) {
            return self::fromEntitie($entity);
        }, $entities);
    }
}