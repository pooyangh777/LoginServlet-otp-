package dataBase;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConnectionProvider implements DataAccessObject<RefreshDTO> {
    private final Connection connection;
    private static final String INSERT = "INSERT INTO refresh (keyId , refreshToken) VALUES (?, ?)";
    private static final String GET_ONE = "SELECT refreshToken FROM refresh WHERE keyID = ?";
    private static final String UPDATE = "UPDATE refresh SET keyId = ?,refreshToken = ?";
    private static final String DELETE = "DELETE FROM refresh WHERE keyId = ?";
    private static final String SELECT = "SELECT * FROM refresh";


    public ConnectionProvider(Connection connection) {
        this.connection = connection;
    }

    @Override
    public RefreshDTO findByKeyId(String keyId) {
        RefreshDTO refreshDTO = new RefreshDTO();
        try {
            PreparedStatement statement = this.connection.prepareStatement(GET_ONE);
            statement.setString(1, keyId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                refreshDTO.setRefreshToken(resultSet.getString("refreshToken"));
            }
            refreshDTO.setKeyId(keyId);
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return refreshDTO;
    }

    @Override
    public RefreshDTO create(RefreshDTO dto) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(INSERT);
            statement.setString(1, dto.getKeyId());
            statement.setString(2, dto.getRefreshToken());
            statement.execute();
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findByKeyId(dto.getKeyId());
    }

    @Override
    public RefreshDTO update(RefreshDTO dto) {
        RefreshDTO refreshDTO = null;
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE);) {
            statement.setString(1, dto.getKeyId());
            statement.setString(2, dto.getRefreshToken());
            statement.execute();
            refreshDTO = this.findByKeyId(dto.getKeyId());
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return refreshDTO;
    }

    @Override
    public void delete(String keyId) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(DELETE);
            statement.setString(1, keyId);
            statement.execute();
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RefreshDTO> selectAll() {
        RefreshDTO refreshDTO = new RefreshDTO();
        List<RefreshDTO> refreshDTOS = new ArrayList<>();
        try {
            PreparedStatement statement = this.connection.prepareStatement(SELECT);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                refreshDTO.setKeyId(resultSet.getString("keyId"));
                refreshDTO.setRefreshToken(resultSet.getString("refreshToken"));
                refreshDTOS.add(refreshDTO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return refreshDTOS;
    }
}
