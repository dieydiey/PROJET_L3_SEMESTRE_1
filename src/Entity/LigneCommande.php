<?php

namespace App\Entity;

use App\Repository\LigneCommandeRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LigneCommandeRepository::class)]
#[ORM\Table(name: 'ligne_commande')]
#[ORM\Index(name: 'idx_ligne_commande', columns: ['id_commande'])]
class LigneCommande
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_ligne')]
    private ?int $idLigne = null;

    #[ORM\Column(name: 'id_commande')]
    private ?int $idCommande = null;

    #[ORM\Column(name: 'id_burger', nullable: true)]
    private ?int $idBurger = null;

    #[ORM\Column(name: 'id_menu', nullable: true)]
    private ?int $idMenu = null;

    #[ORM\Column(name: 'id_complement', nullable: true)]
    private ?int $idComplement = null;

    #[ORM\Column]
    private ?int $quantite = 1;

    #[ORM\Column(name: 'prix_unitaire', type: 'decimal', precision: 10, scale: 2)]
    private ?string $prixUnitaire = null;

    #[ORM\Column(name: 'sous_total', type: 'decimal', precision: 10, scale: 2)]
    private ?string $sousTotal = null;

    #[ORM\ManyToOne(targetEntity: Commande::class, inversedBy: 'lignesCommande')]
    #[ORM\JoinColumn(name: 'id_commande', referencedColumnName: 'id_commande', nullable: false)]
    private ?Commande $commande = null;

    #[ORM\ManyToOne(targetEntity: Burger::class, inversedBy: 'lignesCommande')]
    #[ORM\JoinColumn(name: 'id_burger', referencedColumnName: 'id_burger', nullable: true)]
    private ?Burger $burger = null;

    #[ORM\ManyToOne(targetEntity: Menu::class, inversedBy: 'lignesCommande')]
    #[ORM\JoinColumn(name: 'id_menu', referencedColumnName: 'id_menu', nullable: true)]
    private ?Menu $menu = null;

    #[ORM\ManyToOne(targetEntity: Complement::class, inversedBy: 'lignesCommande')]
    #[ORM\JoinColumn(name: 'id_complement', referencedColumnName: 'id_complement', nullable: true)]
    private ?Complement $complement = null;

    public function getIdLigne(): ?int
    {
        return $this->idLigne;
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

    public function getIdBurger(): ?int
    {
        return $this->idBurger;
    }

    public function setIdBurger(?int $idBurger): self
    {
        $this->idBurger = $idBurger;
        return $this;
    }

    public function getIdMenu(): ?int
    {
        return $this->idMenu;
    }

    public function setIdMenu(?int $idMenu): self
    {
        $this->idMenu = $idMenu;
        return $this;
    }

    public function getIdComplement(): ?int
    {
        return $this->idComplement;
    }

    public function setIdComplement(?int $idComplement): self
    {
        $this->idComplement = $idComplement;
        return $this;
    }

    public function getQuantite(): ?int
    {
        return $this->quantite;
    }

    public function setQuantite(int $quantite): self
    {
        $this->quantite = $quantite;
        return $this;
    }

    public function getPrixUnitaire(): ?float
    {
        return $this->prixUnitaire ? (float) $this->prixUnitaire : null;
    }

    public function setPrixUnitaire(float $prixUnitaire): self
    {
        $this->prixUnitaire = (string) $prixUnitaire;
        return $this;
    }

    public function getSousTotal(): ?float
    {
        return $this->sousTotal ? (float) $this->sousTotal : null;
    }

    public function setSousTotal(float $sousTotal): self
    {
        $this->sousTotal = (string) $sousTotal;
        return $this;
    }

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function getBurger(): ?Burger
    {
        return $this->burger;
    }

    public function getMenu(): ?Menu
    {
        return $this->menu;
    }

    public function getComplement(): ?Complement
    {
        return $this->complement;
    }
}
