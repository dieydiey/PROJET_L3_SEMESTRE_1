<?php
namespace App\Entity;

use App\Repository\PaiementRepository;
use App\Enum\MethodePaiement;
use App\Enum\StatutPaiement;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: PaiementRepository::class)]
#[ORM\Table(name: 'paiement')]
#[ORM\Index(name: 'idx_paiement_commande', columns: ['id_commande'])]
#[ORM\Index(name: 'idx_paiement_statut', columns: ['statut'])]
class Paiement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_paiement')]
    private ?int $idPaiement = null;

    #[ORM\Column(name: 'id_commande', unique: true)]
    private ?int $idCommande = null;

    #[ORM\Column(name: 'date_paiement', type: 'datetime')]
    private ?\DateTimeInterface $datePaiement = null;

    #[ORM\Column(type: 'decimal', precision: 10, scale: 2)]
    private ?string $montant = null;

    #[ORM\Column(name: 'methode_paiement', length: 20, enumType: MethodePaiement::class)]
    private ?MethodePaiement $methodePaiement = null;

    #[ORM\Column(length: 20, enumType: StatutPaiement::class)]
    private ?StatutPaiement $statut = null;

    #[ORM\Column(name: 'reference_transaction', length: 100, nullable: true)]
    private ?string $referenceTransaction = null;

    #[ORM\OneToOne(targetEntity: Commande::class, inversedBy: 'paiement')]
    #[ORM\JoinColumn(name: 'id_commande', referencedColumnName: 'id_commande', nullable: false)]
    private ?Commande $commande = null;

    public function __construct()
    {
        $this->datePaiement = new \DateTime();
        $this->statut = StatutPaiement::EN_ATTENTE;
    }

    public function getIdPaiement(): ?int
    {
        return $this->idPaiement;
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

    public function getDatePaiement(): ?\DateTimeInterface
    {
        return $this->datePaiement;
    }

    public function setDatePaiement(\DateTimeInterface $datePaiement): self
    {
        $this->datePaiement = $datePaiement;
        return $this;
    }

    public function getMontant(): ?float
    {
        return $this->montant ? (float) $this->montant : null;
    }

    public function setMontant(float $montant): self
    {
        $this->montant = (string) $montant;
        return $this;
    }

    public function getMethodePaiement(): ?MethodePaiement
    {
        return $this->methodePaiement;
    }

    public function setMethodePaiement(MethodePaiement $methodePaiement): self
    {
        $this->methodePaiement = $methodePaiement;
        return $this;
    }

    public function getStatut(): ?StatutPaiement
    {
        return $this->statut;
    }

    public function setStatut(StatutPaiement $statut): self
    {
        $this->statut = $statut;
        return $this;
    }

    public function getReferenceTransaction(): ?string
    {
        return $this->referenceTransaction;
    }

    public function setReferenceTransaction(?string $referenceTransaction): self
    {
        $this->referenceTransaction = $referenceTransaction;
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