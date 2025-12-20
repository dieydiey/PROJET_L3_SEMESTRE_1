using Npgsql;
using BrasilBurger.Models;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Data;

namespace BrasilBurger.Repositories.Impl
{
    public class ComplementRepository : IComplementRepository
    {
        private readonly DatabaseConfig _dbConfig;

        public ComplementRepository(DatabaseConfig dbConfig)
        {
            _dbConfig = dbConfig;
        }

        public List<Complement> GetAll()
        {
            var complements = new List<Complement>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_complement, nom, type, prix, image, 
                                archive, date_creation, date_modification 
                                FROM complement 
                                ORDER BY type, nom";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        complements.Add(MapComplement(reader));
                    }
                }
            }
            
            return complements;
        }

        public List<Complement> GetNonArchived()
        {
            var complements = new List<Complement>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_complement, nom, type, prix, image, 
                                archive, date_creation, date_modification 
                                FROM complement 
                                WHERE archive = false 
                                ORDER BY type, nom";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                using (var reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        complements.Add(MapComplement(reader));
                    }
                }
            }
            
            return complements;
        }

        public Complement? GetById(int id)
        {
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_complement, nom, type, prix, image, 
                                archive, date_creation, date_modification 
                                FROM complement 
                                WHERE id_complement = @id";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@id", id);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return MapComplement(reader);
                        }
                    }
                }
            }
            
            return null;
        }

        public List<Complement> GetByType(TypeComplement type)
        {
            var complements = new List<Complement>();
            
            using (var conn = _dbConfig.GetConnection())
            {
                conn.Open();
                string query = @"SELECT id_complement, nom, type, prix, image, 
                                archive, date_creation, date_modification 
                                FROM complement 
                                WHERE type = @type AND archive = false 
                                ORDER BY nom";
                
                using (var cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("@type", type);
                    
                    using (var reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            complements.Add(MapComplement(reader));
                        }
                    }
                }
            }
            
            return complements;
        }

        
        private Complement MapComplement(NpgsqlDataReader reader)
        {
            return new Complement
            {
                IdComplement = reader.GetInt32(0),
                Nom = reader.GetString(1),
                Type = Enum.Parse<TypeComplement>(reader.GetString(2), true),
                Prix = reader.GetDecimal(3),
                Image = reader.IsDBNull(4) ? null : reader.GetString(4),
                Archive = reader.GetBoolean(5),
                DateCreation = reader.GetDateTime(6),
                DateModification = reader.GetDateTime(7)
            };
        }
    }
}