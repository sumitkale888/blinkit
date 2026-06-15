import { View, StyleSheet, Image, Alert } from 'react-native';
import React, { FC, useEffect, useCallback } from 'react';
import { Colors } from '@utils/Constants';
import Logo from '@assets/images/splash_logo.jpeg';
import { screenHeight, screenWidth } from '@utils/Scaling';
import { useAuthStore } from '@state/authStore';
import { tokenStorage } from '@state/storage';
import { resetAndNavigate } from '@utils/NavigationUtils';
import { fetchCurrentUser } from '@service/authService';

const SplashScreen: FC = () => {
    const { user, setUser } = useAuthStore();

    const tokenCheck = useCallback(async () => {
        const accessToken = tokenStorage.getString('accessToken');

        if (!accessToken) {
            resetAndNavigate('CustomerLogin');
            return false;
        }

        try {
            const currentUser = await fetchCurrentUser();
            if (currentUser) {
                setUser(currentUser);
            }

            const role = currentUser?.role || user?.role;
            if (role === 'ROLE_ADMIN') {
                resetAndNavigate('DeliveryDashboard');
            } else {
                resetAndNavigate('ProductDashboard');
            }
            return true;
        } catch (error) {
            tokenStorage.clearAll();
            useAuthStore.getState().logout();
            resetAndNavigate('CustomerLogin');
            return false;
        }
    }, [setUser, user]);

    useEffect(() => {
        const run = async () => {
            try {
                await tokenCheck();
            } catch {
                Alert.alert('Unable to verify session. Please login again.');
            }
        };
        const timeoutId = setTimeout(run, 1000);
        return () => clearTimeout(timeoutId);
    }, [tokenCheck]);

    return (
        <View style={styles.container}>
            <Image source={Logo} style={styles.logoImage} />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        backgroundColor: Colors.primary,
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    logoImage: {
        height: screenHeight * 0.7,
        width: screenWidth * 0.7,
        resizeMode: 'contain',
    },
});

export default SplashScreen;
