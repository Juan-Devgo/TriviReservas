package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdatePasswordDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.application.mapper.AbstractUserMapper;
import co.edu.uniquindio.trivireservas.application.mapper.HostMapper;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.application.ports.out.AbstractUserRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.Host;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.domain.AbstractUser;
import co.edu.uniquindio.trivireservas.domain.UserRole;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.HostEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AbstractUserRepository implements AbstractUserRepositoryUseCases {

    private final AbstractUserJpaRepository abstractUserJpaRepository;

    private final AbstractUserMapper abstractUserMapper;

    private final UserJpaRepository userJpaRepository;

    private final HostJpaRepository hostJpaRepository;

    private final UserMapper userMapper;

    private final HostMapper hostMapper;

    @Override
    public PageResponse<AbstractUser> getUsers(int page) {

        Pageable pageable = PageRequest.of(page, 10);

        Page<AbstractUserEntity> abstractUsersPage = abstractUserJpaRepository.findAll(pageable);
        List<AbstractUserEntity> abstractUserEntities = abstractUsersPage.getContent();

        List<UserEntity> userEntities = abstractUserEntities
                .stream().filter(abs -> abs.getRole().equals(UserRole.USER))
                .map(u -> (UserEntity) u).toList();

        List<HostEntity> hostsEntities = abstractUserEntities
                .stream().filter(abs -> abs.getRole().equals(UserRole.HOST))
                .map(h -> (HostEntity) h).toList();

        List<AbstractUser> abstractUsers = new ArrayList<>(userMapper.toDomainFromEntityList(userEntities));

        abstractUsers.addAll(hostMapper.toDomainFromEntityList(hostsEntities));

        return new PageResponse<>(
                abstractUsers,
                page,
                10,
                abstractUsersPage.getTotalPages(),
                abstractUsersPage.hasNext()
        );
    }

    @Override
    public AbstractUser getAbstractUserByUUID(UUID uuid) {

        Optional<AbstractUserEntity> optionalEntity = abstractUserJpaRepository.findById(uuid);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(uuid.toString());
        }

        return userMapper.toDomainFromEntity(userMapper.castToUserEntity(optionalEntity.get()));
    }

    @Override
    public AbstractUser getAbstractUserByEmail(String email) {

        Optional<AbstractUserEntity> optionalEntity = abstractUserJpaRepository.findByEmail(email);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(email);
        }

        return userMapper.toDomainFromEntity(userMapper.castToUserEntity(optionalEntity.get()));
    }

    @Override
    public AbstractUser getAbstractUserByPhone(String phone) {

        Optional<AbstractUserEntity> optionalEntity = abstractUserJpaRepository.findByPhone(phone);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(phone);
        }

        return userMapper.toDomainFromEntity(userMapper.castToUserEntity(optionalEntity.get()));
    }

    @Override
    @Transactional(readOnly = true)
    public AbstractUser getUserByUUID(UUID uuid) {

        Optional<AbstractUserEntity> optionalEntity = abstractUserJpaRepository.findById(uuid);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(uuid.toString());
        }

        AbstractUserEntity entity = optionalEntity.get();

        if(entity instanceof UserEntity e){
            ((UserEntity) entity).getFavorites().size();
            return userMapper.toDomainFromEntity(e);
        }else

        if(entity instanceof HostEntity e){
            return hostMapper.toDomainFromEntity(e);
        }

        return null;

    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {

        Optional<UserEntity> optionalEntity = userJpaRepository.findByEmail(email);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(email);
        }

        UserEntity entity = optionalEntity.get();

        // Evita un LazyInitializationException al cargar los alojamientos dentro del contexto de Hibernate.
        entity.getFavorites().size();

        return userMapper.toDomainFromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByPhone(String phone) {

        Optional<UserEntity> optionalEntity = userJpaRepository.findByPhone(phone);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(phone);
        }

        UserEntity entity = optionalEntity.get();

        // Evita un LazyInitializationException al cargar los alojamientos dentro del contexto de Hibernate.
        entity.getFavorites().size();

        return userMapper.toDomainFromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Host getHostByUUID(UUID uuid) {

        Optional<HostEntity> optionalEntity = hostJpaRepository.findById(uuid);

        if(optionalEntity.isEmpty()) {
            throw  new EntityNotFoundException(uuid.toString());
        }

        HostEntity entity = optionalEntity.get();

        // Evita un LazyInitializationException al cargar los alojamientos dentro del contexto de Hibernate.
        entity.getLodgings().size();

        return hostMapper.toDomainFromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Host getHostByEmail(String email) {

        Optional<HostEntity> optionalEntity = hostJpaRepository.findByEmail(email);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(email);
        }

        HostEntity entity = optionalEntity.get();

        // Evita un LazyInitializationException al cargar los alojamientos dentro del contexto de Hibernate.
        entity.getLodgings().size();

        return hostMapper.toDomainFromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Host getHostByPhone(String phone) {

        Optional<HostEntity> optionalEntity = hostJpaRepository.findByPhone(phone);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(phone);
        }

        HostEntity entity = optionalEntity.get();

        // Evita un LazyInitializationException al cargar los alojamientos dentro del contexto de Hibernate.
        entity.getLodgings().size();

        return hostMapper.toDomainFromEntity(entity);
    }

    @Override
    public Void createUser(AbstractUserEntity user) {

        if(user.getRole().equals(UserRole.USER)) {

            userJpaRepository.save(userMapper.castToUserEntity(user));

        } else if(user.getRole().equals(UserRole.HOST)) {

            hostJpaRepository.save(hostMapper.castToHostEntity(user));

        } else {
            throw new IllegalArgumentException("Invalid role");
        }

        return null;
    }

    @Override
    public boolean doesEmailExist(String email) {

        Optional<HostEntity> optionalHostEntity = hostJpaRepository.findByEmail(email);

        boolean hostExist = optionalHostEntity.isPresent();

        Optional<UserEntity> optionalUserEntity = userJpaRepository.findByEmail(email);

        boolean userExist = optionalUserEntity.isPresent();

        return userExist || hostExist;
    }

    @Override
    public boolean doesPhoneExist(String phone) {

        Optional<HostEntity> optionalHostEntity = hostJpaRepository.findByPhone(phone);

        boolean hostExist = optionalHostEntity.isPresent();

        Optional<UserEntity> optionalUserEntity = userJpaRepository.findByPhone(phone);

        boolean userExist = optionalUserEntity.isPresent();

        return userExist || hostExist;
    }

    @Override
    public Void updateUser(UUID userUUID, UpdateUserDTO dto) {

        Optional<AbstractUserEntity> optionalEntity = abstractUserJpaRepository.findById(userUUID);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(userUUID.toString());
        }

        AbstractUserEntity abstractUserEntity = optionalEntity.get();

        if(abstractUserEntity.getRole().equals(UserRole.USER)) {

            UserEntity userEntity = (UserEntity) abstractUserEntity;

            userMapper.updateUserEntity(dto, userEntity);

            userJpaRepository.save(userEntity);
            
        } else if (abstractUserEntity.getRole().equals(UserRole.HOST)) {

            HostEntity hostEntity = (HostEntity) abstractUserEntity;

            hostMapper.updateHostEntity(dto, hostEntity);

            hostJpaRepository.save(hostEntity);
            
        } else {
            throw new IllegalArgumentException("Invalid role");
        }

        return null;
    }

    @Override
    public Void updatePassword(UUID userUUID, String newPassword) {

        Optional<AbstractUserEntity> optionalEntity = abstractUserJpaRepository.findById(userUUID);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(userUUID.toString());
        }

        AbstractUserEntity abstractUserEntity = optionalEntity.get();

        if(abstractUserEntity.getRole().equals(UserRole.USER)) {

            UserEntity userEntity = (UserEntity) abstractUserEntity;

            userEntity.setPassword(newPassword);

            userJpaRepository.save(userEntity);

        } else if (abstractUserEntity.getRole().equals(UserRole.HOST)) {

            HostEntity hostEntity = (HostEntity) abstractUserEntity;

            hostEntity.setPassword(newPassword);

            hostJpaRepository.save(hostEntity);

        } else {
            throw new IllegalArgumentException("Invalid role");
        }

        return null;
    }
}