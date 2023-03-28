package com.example.service.impl;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.helper.StringHelper;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public User saveOrUpdate(User entity) {
		logger.info("saveOrUpdate");
		if (StringHelper.equals(entity.getId(), "") || !StringHelper.notNull(entity.getId())) {
			entity.setCreateDay(new Date());
			entity.setUpdateDate(new Date());
		} else {
			entity.setAccountType(findById(entity.getId()).getAccountType());
			entity.setCreateDay(findById(entity.getId()).getCreateDay());
			entity.setUpdateDate(new Date());
		}
		User user = userRepository.save(entity);
		return user;
	}

	@Override
	public User findById(String id) {
		logger.info("findById");
		if (!StringHelper.notNull(id))
			return null;
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty())
			return null;
		return user.get();
	}

	@Override
	public User findByEmail(String email) {
		logger.info("findByEmail");
		if (!StringHelper.notNull(email))
			return null;
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty())
			return null;
		return user.get();
	}

	@Override
	public void deleteById(String id) {
		logger.info("deleteById");
		userRepository.deleteById(id);
	}

	@Override
	public Page<User> findAllByFullNameLike(String userpagekeyword, Pageable pageable) {
		logger.info("findAllByFullNameLike");
		Page<User> page = userRepository.findAllByFullNameLike(userpagekeyword, pageable);
		return page;
	}

	@Override
	public User checkLogin(String email, String password) {
		logger.info("checkLogin");
		Optional<User> user = userRepository.findByEmailAndPassword(email, password);
		if (user.isEmpty())
			return null;
		return user.get();
	}

}
