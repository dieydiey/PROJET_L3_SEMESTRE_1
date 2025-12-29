<?php
namespace App\Enum;

enum EtatCommande: string
{
    case EN_ATTENTE = 'en_attente';
    case EN_COURS = 'en_cours';
    case PRETE = 'prete';
    case TERMINEE = 'terminee';
    case ANNULEE = 'annulee';

    public function getLabel(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'En attente',
            self::EN_COURS => 'En cours',
            self::PRETE => 'Prête',
            self::TERMINEE => 'Terminée',
            self::ANNULEE => 'Annulée',
        };
    }

    public function getBadgeClass(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'warning',
            self::EN_COURS => 'info',
            self::PRETE => 'primary',
            self::TERMINEE => 'success',
            self::ANNULEE => 'danger',
        };
    }

    public function canBeModified(): bool
    {
        return !in_array($this, [self::TERMINEE, self::ANNULEE]);
    }

    public function canBeCancelled(): bool
    {
        return !in_array($this, [self::TERMINEE, self::ANNULEE]);
    }
}