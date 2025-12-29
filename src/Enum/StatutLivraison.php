<?php
namespace App\Enum;

enum StatutLivraison: string
{
    case EN_ATTENTE = 'en_attente';
    case AFFECTEE = 'affectee';
    case EN_COURS = 'en_cours';
    case LIVREE = 'livree';

    public function getLabel(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'En attente',
            self::AFFECTEE => 'Affectée',
            self::EN_COURS => 'En cours',
            self::LIVREE => 'Livrée',
        };
    }

    public function getBadgeClass(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'warning',
            self::AFFECTEE => 'info',
            self::EN_COURS => 'primary',
            self::LIVREE => 'success',
        };
    }

    public function canBeModified(): bool
    {
        return $this !== self::LIVREE;
    }

    public function requiresLivreur(): bool
    {
        return in_array($this, [self::AFFECTEE, self::EN_COURS, self::LIVREE]);
    }

    public function getNextStatus(): ?self
    {
        return match($this) {
            self::EN_ATTENTE => self::AFFECTEE,
            self::AFFECTEE => self::EN_COURS,
            self::EN_COURS => self::LIVREE,
            self::LIVREE => null,
        };
    }
}
