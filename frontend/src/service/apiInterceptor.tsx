import axios from 'axios';
import { BASE_URL } from './config';
import { tokenStorage } from '@state/storage';
import { refresh_tokens } from './authService';
import { Alert } from 'react-native';





export const appAxios = axios.create({
    baseURL: BASE_URL,
});





appAxios.interceptors.request.use(config => {
    const accessToken = tokenStorage.getString('accessToken');
    if (accessToken && config.headers) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
});





appAxios.interceptors.response.use(
    response => response,
    async error => {
        if (error.response && error.response.status === 401) {
            await refresh_tokens();
        } else if (error.response) {
            const errorMessage = error.response.data?.message || 'Something went wrong';
            Alert.alert(errorMessage);
        }

        return Promise.reject(error);
    }
);
