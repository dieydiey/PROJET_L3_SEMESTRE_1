<?php

namespace App\Entity;

use App\Repository\CommandeRepository;
use App\Enum\ModeConsommation;
use App\Enum\EtatCommande;
use App\Entity\Utilisateur;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CommandeRepository::class)]
#[ORM\Table(name: 'commande')]
#[ORM\Index(name: 'idx_commande_client', columns: ['id_client'])]
#[ORM\Index(name: 'idx_commande_date', columns: ['date_commande'])]
#[ORM\Index(name: 'idx_commande_etat', columns: ['etat'])]
#[ORM\Index(name: 'idx_commande_numero', columns: ['numero_commande'])]
class Commande
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_commande')]
    private ?int $idCommande = null;

    #[ORM\Column(name: 'numero_commande', length: 50, unique: true)]
    private ?string $numeroCommande = null;

    /**
     * Relation vers l'utilisateur (Client)
     * On garde le nom de colonne 'id_client' pour la base de données
     */
    #[ORM\ManyToOne(targetEntity: Utilisateur::class)]
    #[ORM\JoinColumn(name: 'id_client', referencedColumnName: 'id_utilisateur', nullable: false)]
    private ?Utilisateur $client = null;

    #[ORM\Column(name: 'date_commande', type: 'datetime')]
    private ?\DateTimeInterface $dateCommande = null;

    #[ORM\Column(name: 'mode_consommation', length: 20, enumType: ModeConsommation::class)]
    private ?ModeConsommation $modeConsommation = null;

    #[ORM\Column(length: 20, enumType: EtatCommande::class)]
    private ?EtatCommande $etat = null;

    #[ORM\Column(name: 'montant_total', type: 'decimal', precision: 10, scale: 2)]
    private ?string $montantTotal = null;

    #[ORM\ManyToOne(targetEntity: Zone::class, inversedBy: 'commandes')]
    #[ORM\JoinColumn(name: 'id_zone', referencedColumnName: 'id_zone', nullable: true)]
    private ?Zone $zone = null;

    #[ORM\OneToMany(mappedBy: 'commande', targetEntity: LigneCommande::class, cascade: ['persist', 'remove'])]
    private Collection $lignesCommande;

    #[ORM\OneToOne(mappedBy: 'commande', targetEntity: Paiement::class)]
    private ?Paiement $paiement = null;

    #[ORM\OneToOne(mappedBy: 'commande', targetEntity: Livraison::class)]
    private ?Livraison $livraison = null;

    public function __construct()
    {
        $this->dateCommande = new \DateTime();
        $this->etat = EtatCommande::EN_ATTENTE;
        $this->lignesCommande = new ArrayCollection();
    }

    // --- GETTERS ET SETTERS ---

    public function getIdCommande(): ?int
    {
        return $this->idCommande;
    }

    public function getNumeroCommande(): ?string
    {
        return $this->numeroCommande;
    }

    public function setNumeroCommande(string $numeroCommande): self
    {
        $this->numeroCommande = $numeroCommande;
        return $this;
    }

    /**
     * Cette méthode est cruciale pour votre DTO actuel
     */
    public function getClient(): ?Utilisateur
    {
        return $this->client;
    }

    public function setClient(?Utilisateur $client): self
    {
        $this->client = $client;
        return $this;
    }

    /**
     * Retourne l'ID du client pour la compatibilité avec votre DTO
     */
    public function getIdClient(): ?int
    {
        return $this->client ? $this->client->getIdUtilisateur() : null;
    }

    public function getDateCommande(): ?\DateTimeInterface
    {
        return $this->dateCommande;
    }

    public function setDateCommande(\DateTimeInterface $dateCommande): self
    {
        $this->dateCommande = $dateCommande;
        return $this;
    }

    public function getModeConsommation(): ?ModeConsommation
    {
        return $this->modeConsommation;
    }

    public function setModeConsommation(ModeConsommation $modeConsommation): self
    {
        $this->modeConsommation = $modeConsommation;
        return $this;
    }

    public function getEtat(): ?EtatCommande
    {
        return $this->etat;
    }

    public function setEtat(EtatCommande $etat): self
    {
        $this->etat = $etat;
        return $this;
    }

    public function getMontantTotal(): ?float
    {
        return $this->montantTotal ? (float) $this->montantTotal : null;
    }

    public function setMontantTotal(float $montantTotal): self
    {
        $this->montantTotal = (string) $montantTotal;
        return $this;
    }

    public function getZone(): ?Zone
    {
        return $this->zone;
    }

    public function setZone(?Zone $zone): self
    {
        $this->zone = $zone;
        return $this;
    }

    public function getIdZone(): ?int
    {
        return $this->zone ? $this->zone->getIdZone() : null;
    }
    
    public function getLignesCommande(): Collection
    {
        return $this->lignesCommande;
    }

    public function addLigneCommande(LigneCommande $ligneCommande): self
    {
        if (!$this->lignesCommande->contains($ligneCommande)) {
            $this->lignesCommande[] = $ligneCommande;
            $ligneCommande->setCommande($this);
        }
        return $this;
    }

    public function getPaiement(): ?Paiement
    {
        return $this->paiement;
    }

    public function getLivraison(): ?Livraison
    {
        return $this->livraison;
    }
}