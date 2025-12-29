<?php
namespace App\DTO;

use DateTimeImmutable;

class FiltreCommandeDto
{
    public ?int $idBurger = null;
    public ?int $idMenu = null;
    public ?DateTimeImmutable $dateCommande = null;
    public ?string $etat = null;
    public ?int $idClient = null;
    public ?string $nomClient = null; // Ajoutez cette ligne
}