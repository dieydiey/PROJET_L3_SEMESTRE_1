<?php
namespace App\DTO;

use App\Entity\Commande;
use DateTimeImmutable;

class CommandeListDto
{
    public ?int $idCommande;
    public string $numeroCommande;
    public int $idClient;
    public ?string $nomClient; // Nouveau champ ajouté
    public DateTimeImmutable $dateCommande;
    public string $modeConsommation;
    public string $etat;
    public float $montantTotal;
    public ?int $idZone;
    public int $nbLignes = 0;

    public static function fromEntity(Commande $entity): CommandeListDto
    {
        $dto = new CommandeListDto();
        $dto->idCommande = $entity->getIdCommande();
        $dto->numeroCommande = $entity->getNumeroCommande();
        $dto->idClient = $entity->getIdClient();
        
        if ($entity->getClient()) {
            $dto->nomClient = $entity->getClient()->getPrenom() . ' ' . $entity->getClient()->getNom();
        } else {
            $dto->nomClient = "Client #" . $entity->getIdClient();
        }

        $dto->dateCommande = DateTimeImmutable::createFromMutable($entity->getDateCommande());
        $dto->modeConsommation = $entity->getModeConsommation()->value;
        $dto->etat = $entity->getEtat()->value;
        $dto->montantTotal = $entity->getMontantTotal();
        $dto->idZone = $entity->getIdZone();
        $dto->nbLignes = count($entity->getLignesCommande());
        
        return $dto;
    }

    public static function fromEntities(array $entities): array
    {
        return array_map(function (Commande $entity) {
            return self::fromEntity($entity);
        }, $entities);
    }
}