<?php
namespace App\Entity;

use App\Repository\CompositionMenuRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CompositionMenuRepository::class)]
#[ORM\Table(name: 'composition_menu')]
#[ORM\Index(name: 'idx_composition_menu', columns: ['id_menu'])]
#[ORM\UniqueConstraint(name: 'unique_composition', columns: ['id_menu', 'id_burger', 'id_complement'])]
class CompositionMenu
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_composition')]
    private ?int $idComposition = null;

    #[ORM\Column(name: 'id_menu')]
    private ?int $idMenu = null;

    #[ORM\Column(name: 'id_burger', nullable: true)]
    private ?int $idBurger = null;

    #[ORM\Column(name: 'id_complement', nullable: true)]
    private ?int $idComplement = null;

    #[ORM\Column]
    private ?int $quantite = 1;

    #[ORM\ManyToOne(targetEntity: Menu::class, inversedBy: 'compositions')]
    #[ORM\JoinColumn(name: 'id_menu', referencedColumnName: 'id_menu', nullable: false)]
    private ?Menu $menu = null;

    public function getIdComposition(): ?int
    {
        return $this->idComposition;
    }

    public function getIdMenu(): ?int
    {
        return $this->idMenu;
    }

    public function setIdMenu(int $idMenu): self
    {
        $this->idMenu = $idMenu;
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

    public function getMenu(): ?Menu
    {
        return $this->menu;
    }

    public function setMenu(?Menu $menu): self
    {
        $this->menu = $menu;
        return $this;
    }
}