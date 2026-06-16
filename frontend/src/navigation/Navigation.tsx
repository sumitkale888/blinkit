import React, { FC } from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { NavigationContainer } from '@react-navigation/native';
import SplashScreen from '@features/auth/SplashScreen';
import { navigationrRef } from '@utils/NavigationUtils';
import DeliveryLogin from '@features/auth/DeliveryLogin';
import CustomerLogin from '@features/auth/CustomerLogin';
import CustomerSignup from '@features/auth/CustomerSignup';
import ProductDashboard from '@features/dashboard/ProductDashboard';
import CategoriesScreen from '@features/catalog/CategoriesScreen';
import CategoryProductsScreen from '@features/catalog/CategoryProductsScreen';
import ProductDetailsScreen from '@features/catalog/ProductDetailsScreen';
import DeliveryDashboard from '@features/delivery/DeliveryDashboard';

const Stack = createNativeStackNavigator();

const Navigation: FC = () => {
    return (
        <NavigationContainer ref={navigationrRef}>
            <Stack.Navigator
                initialRouteName="SplashScreen"
                screenOptions={{
                    headerShown: false,
                }}
            >
                <Stack.Screen name="SplashScreen" component={SplashScreen} />
                <Stack.Screen name="ProductDashboard" component={ProductDashboard} />
                <Stack.Screen name="CategoriesScreen" component={CategoriesScreen} />
                <Stack.Screen name="CategoryProductsScreen" component={CategoryProductsScreen} />
                <Stack.Screen name="ProductDetailsScreen" component={ProductDetailsScreen} />
                <Stack.Screen name="DeliveryDashboard" component={DeliveryDashboard} />
                <Stack.Screen
                    options={{
                        animation: 'slide_from_right',
                    }}
                    name="CustomerSignup"
                    component={CustomerSignup}
                />
                <Stack.Screen
                    options={{
                        animation: 'slide_from_right',
                    }}
                    name="DeliveryLogin"
                    component={DeliveryLogin}
                />
                <Stack.Screen
                    options={{
                        animation: 'slide_from_right',
                    }}
                    name="CustomerLogin"
                    component={CustomerLogin}
                />
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default Navigation;
