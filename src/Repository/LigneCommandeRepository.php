<?php
namespace App\Repository;

use App\Entity\LigneCommande;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class LigneCommandeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, LigneCommande::class);
    }

    public function getTopBurgersByDateRange(\DateTimeInterface $debut, \DateTimeInterface $fin, int $limit = 5): array
    {
        return $this->getEntityManager()->createQuery('
            SELECT b.nom, SUM(lc.quantite) as total
            FROM App\Entity\LigneCommande lc
            JOIN App\Entity\Burger b WITH lc.idBurger = b.idBurger
            JOIN App\Entity\Commande c WITH lc.idCommande = c.idCommande
            WHERE c.dateCommande >= :debut
            AND c.dateCommande < :fin
            AND c.etat != :annulee
            AND lc.idBurger IS NOT NULL
            GROUP BY b.idBurger, b.nom
            ORDER BY total DESC
        ')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('annulee', 'annulee')
            ->setMaxResults($limit)
            ->getResult();
    }

    public function getTopMenusByDateRange(\DateTimeInterface $debut, \DateTimeInterface $fin, int $limit = 5): array
    {
        return $this->getEntityManager()->createQuery('
            SELECT m.nom, SUM(lc.quantite) as total
            FROM App\Entity\LigneCommande lc
            JOIN App\Entity\Menu m WITH lc.idMenu = m.idMenu
            JOIN App\Entity\Commande c WITH lc.idCommande = c.idCommande
            WHERE c.dateCommande >= :debut
            AND c.dateCommande < :fin
            AND c.etat != :annulee
            AND lc.idMenu IS NOT NULL
            GROUP BY m.idMenu, m.nom
            ORDER BY total DESC
        ')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('annulee', 'annulee')
            ->setMaxResults($limit)
            ->getResult();
    }
}