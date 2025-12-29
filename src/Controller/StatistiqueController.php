<?php
namespace App\Controller;

use App\Service\Interface\IStatistiqueServiceInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Attribute\IsGranted;

#[Route('/gestionnaire/statistiques')]
#[IsGranted('ROLE_GESTIONNAIRE')]
class StatistiqueController extends AbstractController
{
    public function __construct(
        private IStatistiqueServiceInterface $statistiqueService
    ) {}

    #[Route('/', name: 'statistique_index', methods: ['GET'])]
    public function index(): Response
    {
        $resume = $this->statistiqueService->resumeJournalier();

        return $this->render('statistique/index.html.twig', [
            'resume' => $resume
        ]);
    }

    #[Route('/periode', name: 'statistique_periode', methods: ['GET', 'POST'])]
    public function periode(Request $request): Response
    {
        $stats = null;

        if ($request->isMethod('POST')) {
            $dateDebut = new \DateTime($request->request->get('dateDebut'));
            $dateFin = new \DateTime($request->request->get('dateFin'));

            if ($dateDebut > $dateFin) {
                $this->addFlash('error', 'La date de début doit être antérieure à la date de fin');
            } else {
                $stats = $this->statistiqueService->statistiquesPeriode($dateDebut, $dateFin);
            }
        }

        return $this->render('statistique/periode.html.twig', [
            'stats' => $stats
        ]);
    }

    #[Route('/export', name: 'statistique_export', methods: ['GET'])]
    public function export(): Response
    {
        $resume = $this->statistiqueService->resumeJournalier();

        $csv = fopen('php://memory', 'w');
        
        fputcsv($csv, ['Statistique', 'Valeur']);
        fputcsv($csv, ['Commandes en cours', $resume['commandesEnCours']]);
        fputcsv($csv, ['Commandes validées', $resume['commandesValidees']]);
        fputcsv($csv, ['Recettes journalières', $resume['recettes'] . ' FCFA']);
        fputcsv($csv, ['Commandes annulées', $resume['commandesAnnulees']]);

        if (isset($resume['topVentes']['burgers'])) {
            foreach ($resume['topVentes']['burgers'] as $burger) {
                fputcsv($csv, ['Top Burger - ' . $burger['nom'], $burger['total']]);
            }
        }

        if (isset($resume['topVentes']['menus'])) {
            foreach ($resume['topVentes']['menus'] as $menu) {
                fputcsv($csv, ['Top Menu - ' . $menu['nom'], $menu['total']]);
            }
        }

        rewind($csv);
        $content = stream_get_contents($csv);
        fclose($csv);

        return new Response($content, 200, [
            'Content-Type' => 'text/csv',
            'Content-Disposition' => 'attachment; filename="statistiques_' . date('Y-m-d') . '.csv"'
        ]);
    }
}
