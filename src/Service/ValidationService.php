<?php
namespace App\Service;

use Symfony\Component\Validator\Validator\ValidatorInterface;
use App\Service\Interface\IValidationServiceInterface;

class ValidationService implements IValidationServiceInterface
{
    public function __construct(
        private ValidatorInterface $validator
    ) {}

    public function valider(object $dto): array
    {
        $violations = $this->validator->validate($dto);
        $errors = [];

        foreach ($violations as $violation) {
            $errors[$violation->getPropertyPath()] = $violation->getMessage();
        }

        return $errors;
    }

    public function estValide(object $dto): bool
    {
        return count($this->valider($dto)) === 0;
    }

    public function validerOuException(object $dto): void
    {
        $errors = $this->valider($dto);
        
        if (!empty($errors)) {
            $message = "Erreurs de validation: " . json_encode($errors);
            throw new \InvalidArgumentException($message);
        }
    }
}