<?php
namespace App\Service\Interface;

use App\DTO\LivraisonDTO;
use App\Entity\Livraison;

interface ILivraisonServiceInterface
{
    public function listerLivraisonsEnAttente(int $page, int $itemsPerPage): array;

    public function regrouperParZone(): array;

    public function affecterLivreur(int $idLivraison, int $idLivreur): bool;

    public function changerStatutLivraison(int $id, string $nouveauStatut): bool;

    public function obtenirLivraisonsLivreur(int $idLivreur, int $page, int $itemsPerPage): array;

    
}
