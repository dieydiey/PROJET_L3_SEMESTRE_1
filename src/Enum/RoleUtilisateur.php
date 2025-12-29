<?php

namespace App\Enum;

enum RoleUtilisateur: string
{
    case CLIENT = 'client';
    case GESTIONNAIRE = 'gestionnaire';

    public function getLabel(): string
    {
        return match($this) {
            self::CLIENT => 'Client',
            self::GESTIONNAIRE => 'Gestionnaire',
        };
    }

    public function getSymfonyRole(): string
    {
        return match($this) {
            self::CLIENT => 'ROLE_CLIENT',
            self::GESTIONNAIRE => 'ROLE_GESTIONNAIRE',
        };
    }
}