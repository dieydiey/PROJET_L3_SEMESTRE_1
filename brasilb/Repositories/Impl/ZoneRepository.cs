using Npgsql;
using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Data;

namespace BrasilBurger.Repositories.Impl
{
    public class ZoneRepository : IZoneRepository
    {
        private readonly DatabaseConfig _dbConfig;

        public ZoneRepository(DatabaseConfig dbConfig)
        {
            _dbConfig = dbConfig;
        }

        public List<Zone> GetAll()
        {
            var zones = new List<Zone>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_zone, nom, quartiers, prix_livraison, date_creation 
                                FROM zone 
                                ORDER BY nom";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        zones.Add(new Zone
                        {
                            IdZone = reader.GetInt32(0),
                            Nom = reader.GetString(1),
                            Quartiers = reader.GetString(2),
                            PrixLivraison = reader.GetDecimal(3),
                            DateCreation = reader.GetDateTime(4)
                        });
                    }
                }
            }
            
            return zones;
        }

        public Zone? GetById(int id)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_zone, nom, quartiers, prix_livraison, date_creation 
                                FROM zone 
                                WHERE id_zone = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", id);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return new Zone
                            {
                                IdZone = reader.GetInt32(0),
                                Nom = reader.GetString(1),
                                Quartiers = reader.GetString(2),
                                PrixLivraison = reader.GetDecimal(3),
                                DateCreation = reader.GetDateTime(4)
                            };
                        }
                    }
                }
            }
            
            return null;
        }
    }
}