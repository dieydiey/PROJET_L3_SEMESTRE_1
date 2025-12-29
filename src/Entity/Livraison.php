<?php
namespace App\Entity;

use App\Repository\LivraisonRepository;
use App\Enum\StatutLivraison;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LivraisonRepository::class)]
#[ORM\Table(name: 'livraison')]
#[ORM\Index(name: 'idx_livraison_livreur', columns: ['id_livreur'])]
#[ORM\Index(name: 'idx_livraison_statut', columns: ['statut'])]
#[ORM\Index(name: 'idx_livraison_commande', columns: ['id_commande'])]
class Livraison
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_livraison')]
    private ?int $idLivraison = null;

    #[ORM\Column(name: 'id_commande', unique: true)]
    private ?int $idCommande = null;

    #[ORM\Column(name: 'id_livreur', nullable: true)]
    private ?int $idLivreur = null;

    #[ORM\Column(name: 'adresse_livraison', type: 'text')]
    private ?string $adresseLivraison = null;

    #[ORM\Column(name: 'date_affectation', type: 'datetime', nullable: true)]
    private ?\DateTimeInterface $dateAffectation = null;

    #[ORM\Column(name: 'date_livraison', type: 'datetime', nullable: true)]
    private ?\DateTimeInterface $dateLivraison = null;

    #[ORM\Column(length: 20, enumType: StatutLivraison::class)]
    private ?StatutLivraison $statut = null;

    #[ORM\OneToOne(targetEntity: Commande::class, inversedBy: 'livraison')]
    #[ORM\JoinColumn(name: 'id_commande', referencedColumnName: 'id_commande', nullable: false)]
    private ?Commande $commande = null;

    public function __construct()
    {
        $this->statut = StatutLivraison::EN_ATTENTE;
    }

    public function getIdLivraison(): ?int
    {
        return $this->idLivraison;
    }

    public function getIdCommande(): ?int
    {
        return $this->idCommande;
    }

    public function setIdCommande(int $idCommande): self
    {
        $this->idCommande = $idCommande;
        return $this;
    }

    public function getIdLivreur(): ?int
    {
        return $this->idLivreur;
    }

    public function setIdLivreur(?int $idLivreur): self
    {
        $this->idLivreur = $idLivreur;
        return $this;
    }

    public function getAdresseLivraison(): ?string
    {
        return $this->adresseLivraison;
    }

    public function setAdresseLivraison(string $adresseLivraison): self
    {
        $this->adresseLivraison = $adresseLivraison;
        return $this;
    }

    public function getDateAffectation(): ?\DateTimeInterface
    {
        return $this->dateAffectation;
    }

    public function setDateAffectation(?\DateTimeInterface $dateAffectation): self
    {
        $this->dateAffectation = $dateAffectation;
        return $this;
    }

    public function getDateLivraison(): ?\DateTimeInterface
    {
        return $this->dateLivraison;
    }

    public function setDateLivraison(?\DateTimeInterface $dateLivraison): self
    {
        $this->dateLivraison = $dateLivraison;
        return $this;
    }

    public function getStatut(): ?StatutLivraison
    {
        return $this->statut;
    }

    public function setStatut(StatutLivraison $statut): self
    {
        $this->statut = $statut;
        return $this;
    }

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(Commande $commande): self
    {
        $this->commande = $commande;
        return $this;
    }
}