<?php

namespace App\Service\Interface;

use App\DTO\CommandeDetailDTO;
use App\DTO\FiltreCommandeDTO;
use App\Entity\Commande;

interface ICommandeServiceInterface
{
   public function listerCommandes(?FiltreCommandeDto $filtre, int $page, int $itemsPerPage): array;

    public function obtenirCommande(int $id): ?CommandeDetailDto;

    public function annulerCommande(int $id): bool;

    public function changerEtatCommande(int $id, string $nouvelEtat): bool;

    public function obtenirCommandesClient(int $idClient, int $page, int $itemsPerPage): array;
}
