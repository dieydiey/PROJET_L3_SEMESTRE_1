<?php
namespace App\Controller;

use App\Repository\LivraisonRepository;
use App\Service\Interface\ILivraisonServiceInterface;
use App\Repository\LivreurRepository;
use App\Repository\ZoneRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Attribute\IsGranted;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;

#[Route('/gestionnaire/livraisons')]
#[IsGranted('ROLE_GESTIONNAIRE')]
class LivraisonController extends AbstractController
{
    public function __construct(
        private ILivraisonServiceInterface $livraisonService,
        private LivreurRepository $livreurRepo,
        private LivraisonRepository $livraisonRepository,
        private ZoneRepository $zoneRepository,
        private ParameterBagInterface $params
    ) {}

    #[Route('/', name: 'livraison_index', methods: ['GET'])]
    public function index(Request $request): Response
    {
        $page = $request->query->get('page', 1);
        $itemsPerPage = $this->params->get('ITEMS_PER_PAGE') ?? 10;
        $offset = ($page - 1) * $itemsPerPage;

        $livraisons = $this->livraisonRepository->findBy(
            ['statut' => 'en_attente'],
            ['idLivraison' => 'ASC'],
            $itemsPerPage,
            $offset
        );

        $totalLivraisons = $this->livraisonRepository->count(['statut' => 'en_attente']);
        $totalPages = ceil($totalLivraisons / $itemsPerPage);

        return $this->render('livraison/index.html.twig', [
            'livraisons' => $livraisons,
            'totalPages' => $totalPages,
            'currentPage' => $page
        ]);
    }

    #[Route('/par-zone', name: 'livraison_par_zone', methods: ['GET'])]
    public function parZone(Request $request): Response
    {
        $zones = $this->zoneRepository->findAll();
        
        $currentZoneId = $request->query->get('zone');
        if (!$currentZoneId && !empty($zones)) {
            $currentZoneId = $zones[0]->getIdZone();
        }

        $activeZone = $currentZoneId ? $this->zoneRepository->find($currentZoneId) : null;

        $livraisons = [];
        if ($currentZoneId) {
            $livraisons = $this->livraisonRepository->findByZoneAndStatut($currentZoneId, 'en_attente');
        }

        $livreurs = $this->livreurRepo->findBy(['disponible' => true]);

        return $this->render('livraison/gestion.html.twig', [
            'zones' => $zones,
            'currentZone' => (int)$currentZoneId,
            'activeZoneName' => $activeZone ? $activeZone->getNom() : 'Aucune zone',
            'livraisons' => $livraisons,
            'livreurs' => $livreurs
        ]);
    }

    #[Route('/affecter-groupe', name: 'livraison_affecter_groupe', methods: ['POST'])]
    public function affecterGroupe(Request $request): Response
    {
        $idLivreur = (int)$request->request->get('idLivreur');
        $idsLivraisons = $request->request->all('livraisons'); // Tableau d'IDs issus des checkboxes

        if (empty($idsLivraisons) || !$idLivreur) {
            $this->addFlash('error', 'Veuillez sélectionner au moins une commande et un livreur.');
            return $this->redirectToRoute('livraison_par_zone');
        }

        try {
            $count = 0;
            foreach ($idsLivraisons as $idLivraison) {
                $this->livraisonService->affecterLivreur((int)$idLivraison, $idLivreur);
                $count++;
            }
            $this->addFlash('success', "$count commande(s) affectée(s) avec succès.");
        } catch (\Exception $e) {
            $this->addFlash('error', 'Erreur lors de l\'affectation : ' . $e->getMessage());
        }

        return $this->redirectToRoute('livraison_par_zone');
    }

    #[Route('/{id}/affecter', name: 'livraison_affecter', methods: ['GET', 'POST'])]
    public function affecter(int $id, Request $request): Response
    {
        $livreurs = $this->livreurRepo->findBy(['disponible' => true]);

        if ($request->isMethod('POST')) {
            $idLivreur = (int)$request->request->get('idLivreur');
            
            if (!$this->isCsrfTokenValid('affecter'.$id, $request->request->get('_token'))) {
                throw $this->createAccessDeniedException('Token CSRF invalide');
            }

            try {
                $this->livraisonService->affecterLivreur($id, $idLivreur);
                $this->addFlash('success', 'Livreur affecté avec succès');
                return $this->redirectToRoute('livraison_index');
            } catch (\Exception $e) {
                $this->addFlash('error', 'Erreur : ' . $e->getMessage());
            }
        }

        return $this->render('livraison/affecter.html.twig', [
            'idLivraison' => $id,
            'livreurs' => $livreurs
        ]);
    }

    #[Route('/{id}/changer-statut', name: 'livraison_changer_statut', methods: ['POST'])]
    public function changerStatut(int $id, Request $request): Response
    {
        if (!$this->isCsrfTokenValid('statut'.$id, $request->request->get('_token'))) {
            throw $this->createAccessDeniedException('Token CSRF invalide');
        }

        $nouveauStatut = $request->request->get('statut');

        try {
            $this->livraisonService->changerStatutLivraison($id, $nouveauStatut);
            $this->addFlash('success', 'Statut modifié avec succès');
        } catch (\Exception $e) {
            $this->addFlash('error', 'Erreur : ' . $e->getMessage());
        }

        return $this->redirectToRoute('livraison_index');
    }

    #[Route('/livreur/{idLivreur}', name: 'livraison_livreur', methods: ['GET'])]
    public function livraisonsLivreur(int $idLivreur, Request $request): Response
    {
        $page = $request->query->get('page', 1);
        $itemsPerPage = $this->params->get('ITEMS_PER_PAGE') ?? 10;
        $offset = ($page - 1) * $itemsPerPage;

        $livraisons = $this->livraisonRepository->findBy(
            ['idLivreur' => $idLivreur],
            ['dateAffectation' => 'DESC'],
            $itemsPerPage,
            $offset
        );

        $totalLivraisons = $this->livraisonRepository->count(['idLivreur' => $idLivreur]);
        $totalPages = ceil($totalLivraisons / $itemsPerPage);

        return $this->render('livraison/livreur.html.twig', [
            'livraisons' => $livraisons,
            'idLivreur' => $idLivreur,
            'totalPages' => $totalPages,
            'currentPage' => $page
        ]);
    }
}