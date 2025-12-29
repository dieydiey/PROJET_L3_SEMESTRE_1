<?php

namespace App\Enum;

enum MethodePaiement: string
{
    case WAVE = 'wave';
    case OM = 'om';

    public function getLabel(): string
    {
        return match($this) {
            self::WAVE => 'Wave',
            self::OM => 'Orange Money',
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::WAVE => 'bi-wallet2',
            self::OM => 'bi-phone',
        };
    }
}