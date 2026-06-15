import axios from 'axios';
import { BASE_URL } from './config';
import { tokenStorage } from '@state/storage';
import { useAuthStore } from '@state/authStore';
import { resetAndNavigate } from '@utils/NavigationUtils';
import { appAxios } from './apiInterceptor';

export const customerLogin = async (email: string, password: string) => {
    try {
        const response = await axios.post(`${BASE_URL}/auth/login`, {
            email,
            password,
        });

        const { accessToken, user } = response.data;
        if (accessToken) {
            tokenStorage.set('accessToken', accessToken);
        }

        useAuthStore.getState().setUser(user);
        return user;
    } catch (error: any) {
        console.log('customer login error', error?.response?.data || error.message || error);
        throw error;
    }
};

export const deliveryLogin = async (email: string, password: string) => {
    try {
        const response = await axios.post(`${BASE_URL}/auth/admin/login`, {
            email,
            password,
        });

        const { accessToken, user } = response.data;
        if (accessToken) {
            tokenStorage.set('accessToken', accessToken);
        }

        useAuthStore.getState().setUser(user);
        return user;
    } catch (error: any) {
        console.log('delivery login error', error?.response?.data || error.message || error);
        throw error;
    }
};

export const customerRegister = async (
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    phoneNumber: string,
) => {
    try {
        const response = await axios.post(`${BASE_URL}/auth/register`, {
            firstName,
            lastName,
            email,
            password,
            phoneNumber,
        });

        const { accessToken, user } = response.data;
        if (accessToken) {
            tokenStorage.set('accessToken', accessToken);
        }

        useAuthStore.getState().setUser(user);
        return user;
    } catch (error: any) {
        console.log('customer register error', error?.response?.data || error.message || error);
        throw error;
    }
};

export const fetchCurrentUser = async () => {
    const response = await appAxios.get('/users/me');
    return response.data;
};

export const fetchAdminUsers = async () => {
    const response = await appAxios.get('/admin/users');
    return response.data?.data ?? [];
};

export const logout = async () => {
    try {
        await appAxios.post('/auth/logout');
    } catch (error) {
        console.log('logout error', error);
    } finally {
        tokenStorage.clearAll();
        useAuthStore.getState().logout();
        resetAndNavigate('CustomerLogin');
    }
};

export const refresh_tokens = async () => {
    tokenStorage.clearAll();
    useAuthStore.getState().logout();
    resetAndNavigate('CustomerLogin');
    return null;
};
