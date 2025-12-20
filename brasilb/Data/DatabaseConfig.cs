using Npgsql;

namespace BrasilBurger.Data
{
    public class DatabaseConfig
    {
        private readonly NpgsqlDataSource _dataSource;

        public DatabaseConfig(NpgsqlDataSource dataSource)
        {
            _dataSource = dataSource;
        }

        public NpgsqlConnection GetConnection()
        {
            return _dataSource.CreateConnection();
        }
    }
}
