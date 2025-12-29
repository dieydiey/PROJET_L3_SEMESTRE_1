<?php

namespace App\Repository;

use App\Entity\Commande;
use App\DTO\FiltreCommandeDto;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class CommandeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commande::class);
    }

    public function save(Commande $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);
        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function findWithFilters(?FiltreCommandeDto $filtre, int $limit, int $offset): array
    {
        $qb = $this->createQueryBuilder('c')
            // Jointure indispensable pour récupérer l'objet Utilisateur
            // et éviter le problème d'undefined method getClient
            ->leftJoin('c.client', 'cl')
            ->addSelect('cl');

        if ($filtre) {
            // Remplacez le bloc if ($filtre->idClient) par celui-ci :
            if ($filtre->idClient) {
                $qb->andWhere('cl.id = :idClient')
                ->setParameter('idClient', $filtre->idClient);
            }

            if ($filtre->nomClient) {
                $qb->andWhere('cl.nom LIKE :nom OR cl.prenom LIKE :nom')
                ->setParameter('nom', '%' . $filtre->nomClient . '%');
            }

            if ($filtre->etat) {
                $qb->andWhere('c.etat = :etat')
                   ->setParameter('etat', $filtre->etat);
            }

            if ($filtre->dateCommande) {
                // On définit le début du jour (00:00:00) et la fin (23:59:59)
                $dateDebut = $filtre->dateCommande->setTime(0, 0, 0);
                $dateFin = $filtre->dateCommande->setTime(23, 59, 59);

                $qb->andWhere('c.dateCommande BETWEEN :debut AND :fin')
                ->setParameter('debut', $dateDebut)
                ->setParameter('fin', $dateFin);
            }

            if ($filtre->idBurger || $filtre->idMenu) {
                $qb->join('c.lignesCommande', 'lc');
                
                if ($filtre->idBurger) {
                    $qb->andWhere('lc.idBurger = :idBurger')
                       ->setParameter('idBurger', $filtre->idBurger);
                }

                if ($filtre->idMenu) {
                    $qb->andWhere('lc.idMenu = :idMenu')
                       ->setParameter('idMenu', $filtre->idMenu);
                }
            }
        }

        return $qb->orderBy('c.dateCommande', 'DESC')
            ->setMaxResults($limit)
            ->setFirstResult($offset)
            ->getQuery()
            ->getResult();
    }

    public function countWithFilters(?FiltreCommandeDto $filtre): int
    {
        $qb = $this->createQueryBuilder('c')
            ->select('COUNT(DISTINCT c.idCommande)')
            ->leftJoin('c.client', 'cl');

        if ($filtre) {
            if ($filtre->idClient) {
                $qb->andWhere('cl.id = :idClient')
                   ->setParameter('idClient', $filtre->idClient);
            }
            // Ajoutez ceci dans la boucle de filtrage du count :
            if ($filtre->nomClient) {
                $qb->andWhere('cl.nom LIKE :nom OR cl.prenom LIKE :nom')
                ->setParameter('nom', '%' . $filtre->nomClient . '%');
            }

            if ($filtre->etat) {
                $qb->andWhere('c.etat = :etat')
                   ->setParameter('etat', $filtre->etat);
            }

            if ($filtre->dateCommande) {
                // On définit le début du jour (00:00:00) et la fin (23:59:59)
                $dateDebut = $filtre->dateCommande->setTime(0, 0, 0);
                $dateFin = $filtre->dateCommande->setTime(23, 59, 59);

                $qb->andWhere('c.dateCommande BETWEEN :debut AND :fin')
                ->setParameter('debut', $dateDebut)
                ->setParameter('fin', $dateFin);
            }

           
        }

        return (int) $qb->getQuery()->getSingleScalarResult();
    }

    public function findByDateRange(\DateTimeInterface $debut, \DateTimeInterface $fin): array
    {
        return $this->createQueryBuilder('c')
            ->leftJoin('c.client', 'cl')
            ->addSelect('cl')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande < :fin')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->orderBy('c.dateCommande', 'DESC')
            ->getQuery()
            ->getResult();
    }

    public function countByDateRangeAndEtat(\DateTimeInterface $debut, \DateTimeInterface $fin, string $etat): int
    {
        return (int) $this->createQueryBuilder('c')
            ->select('COUNT(c.idCommande)')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande < :fin')
            ->andWhere('c.etat = :etat')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('etat', $etat)
            ->getQuery()
            ->getSingleScalarResult();
    }

    public function countByDateRangeAndEtats(\DateTimeInterface $debut, \DateTimeInterface $fin, array $etats): int
    {
        return (int) $this->createQueryBuilder('c')
            ->select('COUNT(c.idCommande)')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande < :fin')
            ->andWhere('c.etat IN (:etats)')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('etats', $etats)
            ->getQuery()
            ->getSingleScalarResult();
    }
}