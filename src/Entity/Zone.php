<?php
namespace App\Entity;

use App\Repository\ZoneRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ZoneRepository::class)]
#[ORM\Table(name: 'zone')]
class Zone
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_zone')]
    private ?int $idZone = null;

    #[ORM\Column(length: 100, unique: true)]
    private ?string $nom = null;

    #[ORM\Column(type: 'text')]
    private ?string $quartiers = null;

    #[ORM\Column(name: 'prix_livraison', type: 'decimal', precision: 10, scale: 2)]
    private ?string $prixLivraison = null;

    #[ORM\Column(name: 'date_creation', type: 'datetime')]
    private ?\DateTimeInterface $dateCreation = null;
    
    #[ORM\OneToMany(mappedBy: 'zone', targetEntity: Commande::class)]
    private Collection $commandes;

    public function __construct()
    {
        $this->dateCreation = new \DateTime();
        $this->commandes = new ArrayCollection();
    }

    public function getIdZone(): ?int
    {
        return $this->idZone;
    }

    /**
     * @return Collection<int, Commande>
     */
    public function getCommandes(): Collection
    {
        return $this->commandes;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    public function getQuartiers(): ?string
    {
        return $this->quartiers;
    }

    public function setQuartiers(string $quartiers): self
    {
        $this->quartiers = $quartiers;
        return $this;
    }

    public function getPrixLivraison(): ?float
    {
        return $this->prixLivraison ? (float) $this->prixLivraison : null;
    }

    public function setPrixLivraison(float $prixLivraison): self
    {
        $this->prixLivraison = (string) $prixLivraison;
        return $this;
    }

    public function getDateCreation(): ?\DateTimeInterface
    {
        return $this->dateCreation;
    }
}