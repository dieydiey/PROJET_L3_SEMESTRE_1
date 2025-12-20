using Npgsql;
using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Data;

namespace BrasilBurger.Repositories.Impl
{
    public class CommandeRepository : ICommandeRepository
    {
        private readonly DatabaseConfig _dbConfig;

    public CommandeRepository(DatabaseConfig dbConfig)
    {
        _dbConfig = dbConfig;
    }

        public int Create(Commande commande)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    INSERT INTO commande (numero_commande, id_client, mode_consommation, 
                                         etat, montant_total, id_zone)
                    VALUES (@numero, @idClient, @mode, @etat, @montant, @idZone)
                    RETURNING id_commande";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@numero", commande.NumeroCommande);
                    cmd.Parameters.AddWithValue("@idClient", commande.IdClient);
                    cmd.Parameters.AddWithValue("@mode", commande.ModeConsommation);
                    cmd.Parameters.AddWithValue("@etat", commande.Etat);

                    cmd.Parameters.AddWithValue("@montant", commande.MontantTotal);
                    cmd.Parameters.AddWithValue("@idZone", (object?)commande.IdZone ?? DBNull.Value);
                    
                    return (int)cmd.ExecuteScalar()!;
                }
            }
        }

        public void AddLigneCommande(LigneCommande ligne)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    INSERT INTO ligne_commande (id_commande, id_burger, id_menu, 
                                                id_complement, quantite, prix_unitaire, sous_total)
                    VALUES (@idCommande, @idBurger, @idMenu, @idComplement, 
                            @quantite, @prixUnitaire, @sousTotal)";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@idCommande", ligne.IdCommande);
                    cmd.Parameters.AddWithValue("@idBurger", (object?)ligne.IdBurger ?? DBNull.Value);
                    cmd.Parameters.AddWithValue("@idMenu", (object?)ligne.IdMenu ?? DBNull.Value);
                    cmd.Parameters.AddWithValue("@idComplement", (object?)ligne.IdComplement ?? DBNull.Value);
                    cmd.Parameters.AddWithValue("@quantite", ligne.Quantite);
                    cmd.Parameters.AddWithValue("@prixUnitaire", ligne.PrixUnitaire);
                    cmd.Parameters.AddWithValue("@sousTotal", ligne.SousTotal);
                    
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public Commande? GetById(int id)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT c.id_commande, c.numero_commande, c.id_client, c.date_commande, 
                           c.mode_consommation, c.etat, c.montant_total, c.id_zone,
                           u.nom, u.prenom, u.email, u.telephone
                    FROM commande c
                    INNER JOIN utilisateur u ON c.id_client = u.id_utilisateur
                    WHERE c.id_commande = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", id);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            var commande = MapCommande(reader);
                            commande.LignesCommande = GetLignesCommande(id);
                            return commande;
                        }
                    }
                }
            }
            
            return null;
        }

        public Commande? GetByNumero(string numero)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT c.id_commande, c.numero_commande, c.id_client, c.date_commande, 
                           c.mode_consommation, c.etat, c.montant_total, c.id_zone,
                           u.nom, u.prenom, u.email, u.telephone
                    FROM commande c
                    INNER JOIN utilisateur u ON c.id_client = u.id_utilisateur
                    WHERE c.numero_commande = @numero";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@numero", numero);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            var commande = MapCommande(reader);
                            commande.LignesCommande = GetLignesCommande(commande.IdCommande);
                            return commande;
                        }
                    }
                }
            }
            
            return null;
        }

        public List<Commande> GetByClient(int idClient)
        {
            var commandes = new List<Commande>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT c.id_commande, c.numero_commande, c.id_client, c.date_commande, 
                           c.mode_consommation, c.etat, c.montant_total, c.id_zone,
                           u.nom, u.prenom, u.email, u.telephone
                    FROM commande c
                    INNER JOIN utilisateur u ON c.id_client = u.id_utilisateur
                    WHERE c.id_client = @idClient
                    ORDER BY c.date_commande DESC";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@idClient", idClient);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var commande = MapCommande(reader);
                            commande.LignesCommande = GetLignesCommande(commande.IdCommande);
                            commandes.Add(commande);
                        }
                    }
                }
            }
            
            return commandes;
        }

        public List<Commande> GetByDate(DateTime date)
        {
            var commandes = new List<Commande>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT c.id_commande, c.numero_commande, c.id_client, c.date_commande, 
                           c.mode_consommation, c.etat, c.montant_total, c.id_zone,
                           u.nom, u.prenom, u.email, u.telephone
                    FROM commande c
                    INNER JOIN utilisateur u ON c.id_client = u.id_utilisateur
                    WHERE DATE(c.date_commande) = @date
                    ORDER BY c.date_commande DESC";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@date", date.Date);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            commandes.Add(MapCommande(reader));
                        }
                    }
                }
            }
            
            return commandes;
        }

        public List<Commande> GetByEtat(EtatCommande etat)
        {
            var commandes = new List<Commande>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT c.id_commande, c.numero_commande, c.id_client, c.date_commande, 
                           c.mode_consommation, c.etat, c.montant_total, c.id_zone,
                           u.nom, u.prenom, u.email, u.telephone
                    FROM commande c
                    INNER JOIN utilisateur u ON c.id_client = u.id_utilisateur
                    WHERE c.etat = @etat
                    ORDER BY c.date_commande DESC";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@etat", etat);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            commandes.Add(MapCommande(reader));
                        }
                    }
                }
            }
            
            return commandes;
        }

        public void UpdateEtat(int idCommande, EtatCommande etat)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = "UPDATE commande SET etat = @etat WHERE id_commande = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", idCommande);
                    cmd.Parameters.AddWithValue("@etat", etat);

                    
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public void Annuler(int idCommande)
        {
            UpdateEtat(idCommande, EtatCommande.Annulee);
        }

        private List<LigneCommande> GetLignesCommande(int idCommande)
        {
            var lignes = new List<LigneCommande>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    SELECT lc.id_ligne, lc.id_commande, lc.id_burger, lc.id_menu, 
                           lc.id_complement, lc.quantite, lc.prix_unitaire, lc.sous_total,
                           b.nom as burger_nom, b.image as burger_image,
                           m.nom as menu_nom, m.image as menu_image,
                           c.nom as complement_nom, c.image as complement_image
                    FROM ligne_commande lc
                    LEFT JOIN burger b ON lc.id_burger = b.id_burger
                    LEFT JOIN menu m ON lc.id_menu = m.id_menu
                    LEFT JOIN complement c ON lc.id_complement = c.id_complement
                    WHERE lc.id_commande = @idCommande";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@idCommande", idCommande);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            var ligne = new LigneCommande
                            {
                                IdLigne = reader.GetInt32(0),
                                IdCommande = reader.GetInt32(1),
                                IdBurger = reader.IsDBNull(2) ? null : reader.GetInt32(2),
                                IdMenu = reader.IsDBNull(3) ? null : reader.GetInt32(3),
                                IdComplement = reader.IsDBNull(4) ? null : reader.GetInt32(4),
                                Quantite = reader.GetInt32(5),
                                PrixUnitaire = reader.GetDecimal(6),
                                SousTotal = reader.GetDecimal(7)
                            };

                            if (!reader.IsDBNull(8))
                            {
                                ligne.Burger = new Burger
                                {
                                    IdBurger = ligne.IdBurger!.Value,
                                    Nom = reader.GetString(8),
                                    Image = reader.IsDBNull(9) ? null : reader.GetString(9)
                                };
                            }

                            if (!reader.IsDBNull(10))
                            {
                                ligne.Menu = new Menu
                                {
                                    IdMenu = ligne.IdMenu!.Value,
                                    Nom = reader.GetString(10),
                                    Image = reader.IsDBNull(11) ? null : reader.GetString(11)
                                };
                            }

                            if (!reader.IsDBNull(12))
                            {
                                ligne.Complement = new Complement
                                {
                                    IdComplement = ligne.IdComplement!.Value,
                                    Nom = reader.GetString(12),
                                    Image = reader.IsDBNull(13) ? null : reader.GetString(13)
                                };
                            }

                            lignes.Add(ligne);
                        }
                    }
                }
            }
            
            return lignes;
        }

        private Commande MapCommande(NpgsqlDataReader reader)
        {
            return new Commande
            {
                IdCommande = reader.GetInt32(0),
                NumeroCommande = reader.GetString(1),
                IdClient = reader.GetInt32(2),
                DateCommande = reader.GetDateTime(3),
                ModeConsommation = reader.GetFieldValue<ModeConsommation>(4),
                Etat = reader.GetFieldValue<EtatCommande>(5),

                MontantTotal = reader.GetDecimal(6),
                IdZone = reader.IsDBNull(7) ? null : reader.GetInt32(7),
                Client = new Utilisateur
                {
                    IdUtilisateur = reader.GetInt32(2),
                    Nom = reader.GetString(8),
                    Prenom = reader.GetString(9),
                    Email = reader.GetString(10),
                    Telephone = reader.GetString(11)
                }
            };
        }
    }
}