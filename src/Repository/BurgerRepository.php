<?php
namespace App\Repository;

use App\Entity\Burger;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class BurgerRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Burger::class);
    }

    public function findAllNonArchived(): array
    {
        return $this->createQueryBuilder('b')
            ->where('b.archive = :archive')
            ->setParameter('archive', false)
            ->orderBy('b.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
}