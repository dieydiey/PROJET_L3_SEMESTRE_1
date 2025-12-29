<?php
namespace App\Service;

use App\Repository\CommandeRepository;
use App\Repository\PaiementRepository;
use App\Repository\LigneCommandeRepository;
use App\Service\Interface\IStatistiqueServiceInterface;

class StatistiqueService implements IStatistiqueServiceInterface
{
    public function __construct(
        private CommandeRepository $commandeRepository,
        private PaiementRepository $paiementRepository,
        private LigneCommandeRepository $ligneCommandeRepository
    ) {}

    public function commandesEnCoursDuJour(): int
    {
        $debut = new \DateTime('today');
        $fin = new \DateTime('tomorrow');

        return $this->commandeRepository->countByDateRangeAndEtats(
            $debut,
            $fin,
            ['en_attente', 'en_cours']
        );
    }

    public function commandesValideesDuJour(): int
    {
        $debut = new \DateTime('today');
        $fin = new \DateTime('tomorrow');

        return $this->commandeRepository->countByDateRangeAndEtat(
            $debut,
            $fin,
            'terminee'
        );
    }

    public function recettesJournalieres(): float
    {
        $debut = new \DateTime('today');
        $fin = new \DateTime('tomorrow');

        return $this->paiementRepository->sumByDateRangeAndStatut(
            $debut,
            $fin,
            'valide'
        );
    }

    public function topVentesDuJour(): array
    {
        $debut = new \DateTime('today');
        $fin = new \DateTime('tomorrow');

        $topBurgers = $this->ligneCommandeRepository->getTopBurgersByDateRange($debut, $fin, 5);
        $topMenus = $this->ligneCommandeRepository->getTopMenusByDateRange($debut, $fin, 5);

        return [
            'burgers' => $topBurgers,
            'menus' => $topMenus
        ];
    }

    public function commandesAnnuleesDuJour(): int
    {
        $debut = new \DateTime('today');
        $fin = new \DateTime('tomorrow');

        return $this->commandeRepository->countByDateRangeAndEtat(
            $debut,
            $fin,
            'annulee'
        );
    }

    public function resumeJournalier(): array
    {
        return [
            'commandesEnCours' => $this->commandesEnCoursDuJour(),
            'commandesValidees' => $this->commandesValideesDuJour(),
            'recettes' => $this->recettesJournalieres(),
            'topVentes' => $this->topVentesDuJour(),
            'commandesAnnulees' => $this->commandesAnnuleesDuJour()
        ];
    }

    public function statistiquesPeriode(\DateTimeInterface $debut, \DateTimeInterface $fin): array
    {
        $commandes = $this->commandeRepository->findByDateRange($debut, $fin);
        
        $recettes = $this->paiementRepository->sumByDateRangeAndStatut(
            $debut,
            $fin,
            'valide'
        );

        return [
            'commandesTotal' => count($commandes),
            'recettes' => $recettes,
            'periode' => [
                'debut' => $debut->format('Y-m-d'),
                'fin' => $fin->format('Y-m-d')
            ]
        ];
    }
}
