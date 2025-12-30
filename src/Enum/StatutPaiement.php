<?php
namespace App\Enum;

enum StatutPaiement: string
{
    case EN_ATTENTE = 'en_attente';
    case VALIDE = 'valide';
    case ECHOUE = 'echoue';

    public function getLabel(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'En attente',
            self::VALIDE => 'Validé',
            self::ECHOUE => 'Échoué',
        };
    }

    public function getBadgeClass(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'warning',
            self::VALIDE => 'success',
            self::ECHOUE => 'danger',
        };
    }

    public function isSuccessful(): bool
    {
        return $this === self::VALIDE;
    }

    public function isFailed(): bool
    {
        return $this === self::ECHOUE;
    }
}