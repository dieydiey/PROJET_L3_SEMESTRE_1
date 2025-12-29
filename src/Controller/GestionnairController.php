<?php
namespace App\Controller;

use App\Service\Interface\IStatistiqueServiceInterface;
use App\Repository\CommandeRepository;
use App\Repository\LivraisonRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Attribute\IsGranted;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

#[Route('/gestionnaire')]
#[IsGranted('ROLE_GESTIONNAIRE')]
class GestionnaireController extends AbstractController
{
    public function __construct(
        private IStatistiqueServiceInterface $statistiqueService,
        private CommandeRepository $commandeRepository,
        private LivraisonRepository $livraisonRepository,
        private EntityManagerInterface $entityManager
    ) {}

    #[Route('/', name: 'gestionnaire_home')]
    public function index(): Response
    {
        return $this->redirectToRoute('statistique_index');
    }

    #[Route('/profil', name: 'gestionnaire_profil', methods: ['GET'])]
    public function profil(): Response
    {
        return $this->render('gestionnaire/profil.html.twig');
    }

    #[Route('/profil/update', name: 'gestionnaire_profil_update', methods: ['POST'])]
    public function updateProfil(Request $request, UserPasswordHasherInterface $passwordHasher): Response
    {
        /** @var User $user */
        $user = $this->getUser();
        
        $prenom = $request->request->get('prenom');
        $nom = $request->request->get('nom');
        $email = $request->request->get('email');
        $telephone = $request->request->get('telephone');
        $newPassword = $request->request->get('new_password');

        $user->setPrenom($prenom);
        $user->setNom($nom);
        $user->setEmail($email);
        $user->setTelephone($telephone);

        if (!empty($newPassword)) {
            $hashedPassword = $passwordHasher->hashPassword($user, $newPassword);
            $user->setPassword($hashedPassword);
        }

        $this->entityManager->flush();

        $this->addFlash('success', 'Votre profil a été mis à jour avec succès.');

        return $this->redirectToRoute('gestionnaire_profil');
    }
}