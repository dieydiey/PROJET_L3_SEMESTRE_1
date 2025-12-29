<?php
namespace App\DTO;

use App\Entity\Livreur;
use DateTimeImmutable;

class LivreurListDto
{
    public ?int $id;
    public string $nom;
    public string $prenom;
    public string $telephone;
    public string $matricule;
    public bool $disponible;
    public DateTimeImmutable $createAt;

    public static function fromEntitie(Livreur $entity): LivreurListDto
    {
        $dto = new LivreurListDto();
        $dto->id = $entity->getIdLivreur();
        $dto->nom = $entity->getNom();
        $dto->prenom = $entity->getPrenom();
        $dto->telephone = $entity->getTelephone();
        $dto->matricule = $entity->getMatricule();
        $dto->disponible = $entity->getDisponible();
        $dto->createAt = DateTimeImmutable::createFromMutable($entity->getDateCreation());
        return $dto;
    }

    public static function fromEntities(array $entities): array
    {
        return array_map(function (Livreur $entity) {
            return self::fromEntitie($entity);
        }, $entities);
    }
}
