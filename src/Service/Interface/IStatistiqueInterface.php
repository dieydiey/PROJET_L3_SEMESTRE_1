<?php
namespace App\Service\Interface;

interface IStatistiqueServiceInterface
{
    public function commandesEnCoursDuJour(): int;

    public function commandesValideesDuJour(): int;

    public function recettesJournalieres(): float;

    public function topVentesDuJour(): array;

    public function commandesAnnuleesDuJour(): int;

    public function resumeJournalier(): array;

    public function statistiquesPeriode(\DateTimeInterface $debut, \DateTimeInterface $fin): array;
    
}