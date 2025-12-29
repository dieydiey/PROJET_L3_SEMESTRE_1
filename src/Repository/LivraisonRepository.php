<?php
namespace App\Repository;

use App\Entity\Livraison;
use App\Enum\StatutLivraison;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class LivraisonRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Livraison::class);
    }

    public function save(Livraison $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);
        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function findByStatutWithPagination(string $statut, int $limit, int $offset): array
    {
        return $this->createQueryBuilder('l')
            ->where('l.statut = :statut')
            ->setParameter('statut', $statut)
            ->orderBy('l.idLivraison', 'ASC')
            ->setMaxResults($limit)
            ->setFirstResult($offset)
            ->getQuery()
            ->getResult();
    }

    
    public function findByLivreurWithPagination(int $idLivreur, int $limit, int $offset): array
    {
        return $this->createQueryBuilder('l')
            ->where('l.idLivreur = :idLivreur')
            ->setParameter('idLivreur', $idLivreur)
            ->orderBy('l.dateAffectation', 'DESC')
            ->setMaxResults($limit)
            ->setFirstResult($offset)
            ->getQuery()
            ->getResult();
    }

    public function findEnAttenteGroupedByZone(): array
    {
        return $this->createQueryBuilder('l')
            ->join('l.commande', 'c')
            ->where('l.statut = :statut')
            ->andWhere('c.idZone IS NOT NULL')
            ->setParameter('statut', StatutLivraison::EN_ATTENTE)
            ->orderBy('c.idZone', 'ASC')
            ->getQuery()
            ->getResult();
    }

    public function findByZoneAndStatut(int $zoneId, string|StatutLivraison $statut): array
    {
        return $this->createQueryBuilder('l')
            ->join('l.commande', 'c')
            ->where('c.zone = :zoneId') // CHANGEMENT ICI : 'zone' au lieu de 'idZone'
            ->andWhere('l.statut = :statut')
            ->setParameter('zoneId', $zoneId)
            ->setParameter('statut', $statut instanceof StatutLivraison ? $statut->value : $statut)
            ->getQuery()
            ->getResult();
    }
}