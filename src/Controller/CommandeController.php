<?php

namespace App\Controller;

use App\DTO\FiltreCommandeDto;
use App\Repository\BurgerRepository;
use App\Repository\MenuRepository;
use App\Service\Interface\ICommandeServiceInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Attribute\IsGranted;


#[Route('/gestionnaire/commandes')]
#[IsGranted('ROLE_GESTIONNAIRE')]
class CommandeController extends AbstractController
{
    public function __construct(
        private ICommandeServiceInterface $commandeService,
        private BurgerRepository $burgerRepository,
        private MenuRepository $menuRepository,
        private ParameterBagInterface $params
    ) {}

    #[Route('/', name: 'commande_index', methods: ['GET'])]
    public function index(Request $request): Response
    {
        $page = (int)$request->query->get('page', 1);
        $itemsPerPage = (int)$this->params->get('ITEMS_PER_PAGE');

        $filtre = new FiltreCommandeDto();
        
        if ($request->query->has('etat')) {
            $filtre->etat = $request->query->get('etat');
        }
        
        if ($request->query->get('nomClient')) {
            $filtre->nomClient = $request->query->get('nomClient');
        }
        
        if ($request->query->get('date')) {
            $filtre->dateCommande = new \DateTimeImmutable($request->query->get('date'));
        }
        
        
        if ($request->query->get('idBurger')) {
            $filtre->idBurger = (int)$request->query->get('idBurger');
        }
        
        if ($request->query->get('idMenu')) {
            $filtre->idMenu = (int)$request->query->get('idMenu');
        }
        
        if ($request->query->get('idClient')) {
            $filtre->idClient = (int)$request->query->get('idClient');
        }

        $resultat = $this->commandeService->listerCommandes($filtre, $page, $itemsPerPage);

        $burgers = $this->burgerRepository->findBy(['archive' => false]);
        $menus = $this->menuRepository->findBy(['archive' => false]);

        return $this->render('commande/index.html.twig', [
            'commandes'   => $resultat['commandes'],
            'totalPages'  => $resultat['totalPages'],
            'currentPage' => $resultat['currentPage'],
            'total'       => $resultat['total'],
            'filtre'      => $filtre,
            'burgers'     => $burgers,
            'menus'       => $menus
        ]);
    }

   #[Route('/{id}', name: 'commande_show', methods: ['GET'])]
    public function show(int $id): Response
    {
        $commande = $this->commandeService->obtenirCommande($id);

        if (!$commande) {
            throw $this->createNotFoundException('Commande non trouvée');
        }

        return $this->render('commande/show.html.twig', [
            'commande' => $commande,
            'totalPages' => 1, 
            'currentPage' => 1
        ]);
    }

    #[Route('/{id}/changer-etat', name: 'commande_changer_etat', methods: ['POST'])]
    public function changerEtat(int $id, Request $request): Response
    {
        if (!$this->isCsrfTokenValid('etat'.$id, $request->request->get('_token'))) {
            throw $this->createAccessDeniedException('Token CSRF invalide');
        }

        $nouvelEtat = $request->request->get('etat');

        try {
            $this->commandeService->changerEtatCommande($id, $nouvelEtat);
            $this->addFlash('success', 'État de la commande modifié avec succès');
        } catch (\Exception $e) {
            $this->addFlash('error', 'Erreur: ' . $e->getMessage());
        }

        return $this->redirectToRoute('commande_show', ['id' => $id]);
    }

    #[Route('/{id}/annuler', name: 'commande_annuler', methods: ['POST'])]
    public function annuler(int $id, Request $request): Response
    {
        if (!$this->isCsrfTokenValid('annuler'.$id, $request->request->get('_token'))) {
            throw $this->createAccessDeniedException('Token CSRF invalide');
        }

        try {
            $this->commandeService->annulerCommande($id);
            $this->addFlash('success', 'Commande annulée avec succès');
        } catch (\Exception $e) {
            $this->addFlash('error', 'Erreur: ' . $e->getMessage());
        }

        return $this->redirectToRoute('commande_index');
    }

    #[Route('/client/{idClient}', name: 'commande_client', methods: ['GET'])]
    public function commandesClient(int $idClient, Request $request): Response
    {
        $page = (int)$request->query->get('page', 1);
        $itemsPerPage = (int)$this->params->get('ITEMS_PER_PAGE');

        $resultat = $this->commandeService->obtenirCommandesClient($idClient, $page, $itemsPerPage);

        return $this->render('commande/client.html.twig', [
            'commandes'   => $resultat['commandes'],
            'idClient'    => $idClient,
            'totalPages'  => $resultat['totalPages'],
            'currentPage' => $resultat['currentPage']
        ]);
    }
}