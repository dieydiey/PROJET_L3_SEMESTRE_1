<?php
namespace App\Repository;

use App\Entity\Paiement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class PaiementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Paiement::class);
    }

    public function sumByDateRangeAndStatut(\DateTimeInterface $debut, \DateTimeInterface $fin, string $statut): float
    {
        $result = $this->createQueryBuilder('p')
            ->select('SUM(p.montant)')
            ->join('p.commande', 'c')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande < :fin')
            ->andWhere('p.statut = :statut')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('statut', $statut)
            ->getQuery()
            ->getSingleScalarResult();

        return $result ? (float) $result : 0.0;
    }
}
