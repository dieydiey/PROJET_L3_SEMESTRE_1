<?php
namespace App\Enum;

enum TypeComplement: string
{
    case BOISSON = 'boisson';
    case FRITES = 'frites';

    public function getLabel(): string
    {
        return match($this) {
            self::BOISSON => 'Boisson',
            self::FRITES => 'Frites',
        };
    }
}