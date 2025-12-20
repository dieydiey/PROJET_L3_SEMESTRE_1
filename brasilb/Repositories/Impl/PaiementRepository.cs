using Npgsql;
using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Data;

namespace BrasilBurger.Repositories.Impl
{
    public class PaiementRepository : IPaiementRepository
    {
        private readonly DatabaseConfig _dbConfig;

        public PaiementRepository(DatabaseConfig dbConfig)
        {
            _dbConfig = dbConfig;
        }

        public int Create(Paiement paiement)
        {
            using (var conn =  _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"
                    INSERT INTO paiement (id_commande, montant, methode_paiement, 
                                         statut, reference_transaction)
                    VALUES (@idCommande, @montant, @methode, @statut, @reference)
                    RETURNING id_paiement";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@idCommande", paiement.IdCommande);
                    cmd.Parameters.AddWithValue("@montant", paiement.Montant);
                    cmd.Parameters.AddWithValue("@methode", paiement.Methode);
                    cmd.Parameters.AddWithValue("@statut", paiement.Statut);
                    cmd.Parameters.AddWithValue("@reference", (object?)paiement.ReferenceTransaction ?? DBNull.Value);
                    
                    return (int)cmd.ExecuteScalar()!;
                }
            }
        }

        public Paiement? GetByCommande(int idCommande)
        {
            using (var conn =  _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_paiement, id_commande, date_paiement, montant, 
                                methode_paiement, statut, reference_transaction 
                                FROM paiement 
                                WHERE id_commande = @idCommande";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@idCommande", idCommande);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return new Paiement
                            {
                                IdPaiement = reader.GetInt32(0),
                                IdCommande = reader.GetInt32(1),
                                DatePaiement = reader.GetDateTime(2),
                                Montant = reader.GetDecimal(3),
                                Methode = reader.GetFieldValue<MethodePaiement>(4),
                                Statut = reader.GetFieldValue<StatutPaiement>(5),
                                ReferenceTransaction = reader.IsDBNull(6) ? null : reader.GetString(6)
                            };
                        }
                    }
                }
            }
            
            return null;
        }

        public void UpdateStatut(int idPaiement, StatutPaiement statut)
        {
            using (var conn =  _dbConfig.GetConnection())
            {
                conn.Open();
                string query = "UPDATE paiement SET statut = @statut WHERE id_paiement = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", idPaiement);
                    cmd.Parameters.AddWithValue("@statut", statut);
                    
                    cmd.ExecuteNonQuery();
                }
            }
        }
    }
}