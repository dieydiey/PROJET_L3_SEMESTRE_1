<?php
namespace App\Service\Interface;

interface IValidationServiceInterface
{
    public function valider(object $dto): array;

    public function estValide(object $dto): bool;

    public function validerOuException(object $dto): void;
}