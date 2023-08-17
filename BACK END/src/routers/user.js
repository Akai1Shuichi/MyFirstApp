const express = require('express');
const router = new express.Router();

const auth = require('../middleware/auth');
const userController = require('../controller/userController');
// Create user
router.post('/user', userController.insert);

// Login user
router.post('/user/login', userController.login);

// Logout user
router.post('/user/logout', auth, userController.logout);

// Check password
router.post('/user/checkpass', auth, userController.checkpass);

// Get User by ID
router.get('/user/you', auth, userController.get);

// Update user
router.patch('/user/you', auth, userController.update);

// Delete user
router.delete('/user/you', auth, userController.delete);

module.exports = router;
