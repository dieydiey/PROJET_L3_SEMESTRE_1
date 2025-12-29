<?php
namespace App\Enum;

enum ModeConsommation: string
{
    case SUR_PLACE = 'sur_place';
    case A_EMPORTER = 'a_emporter';
    case LIVRAISON = 'livraison';

    public function getLabel(): string
    {
        return match($this) {
            self::SUR_PLACE => 'Sur place',
            self::A_EMPORTER => 'À emporter',
            self::LIVRAISON => 'Livraison',
        };
    }

    public function requiresAddress(): bool
    {
        return $this === self::LIVRAISON;
    }

    public function requiresZone(): bool
    {
        return $this === self::LIVRAISON;
    }
}
