import { Alert, Animated, Image, Keyboard, Pressable, SafeAreaView, StyleSheet, View } from 'react-native';
import React, { FC, useEffect, useRef, useState } from 'react';
import { GestureHandlerRootView, PanGestureHandler, State } from 'react-native-gesture-handler';
import CustomSafeAreaView from '@components/global/CustomSafeAreaView';
import ProductSlider from '@components/login/ProductSlider';
import { navigate, resetAndNavigate } from '@utils/NavigationUtils';
import CustomText from '@components/ui/CustomText';
import { Colors, Fonts, lightColors } from '@utils/Constants';
import CustomInput from '@components/ui/CustomInput';
import Button from '@components/ui/Button';
import useKeyboardOffsetHeight from '@utils/useKeyboardOffsetHeight';
import { RFValue } from 'react-native-responsive-fontsize';
import LinearGradient from 'react-native-linear-gradient';
import { customerRegister } from '@service/authService';

const bottomColors = [...lightColors].reverse();

const CustomerSignup: FC = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [loading, setLoading] = useState(false);

    const keyboardOffsetHeight = useKeyboardOffsetHeight();
    const animatedValue = useRef(new Animated.Value(0)).current;

    useEffect(() => {
        if (keyboardOffsetHeight === 0) {
            Animated.timing(animatedValue, {
                toValue: 0,
                duration: 500,
                useNativeDriver: true,
            }).start();
        } else {
            Animated.timing(animatedValue, {
                toValue: -keyboardOffsetHeight * 0.84,
                duration: 600,
                useNativeDriver: true,
            }).start();
        }
    }, [keyboardOffsetHeight, animatedValue]);

    const handleRegister = async () => {
        Keyboard.dismiss();
        setLoading(true);

        if (!firstName.trim() || !lastName.trim() || !email.trim() || password.length < 8) {
            Alert.alert('Validation error', 'Please fill in all fields and use at least 8 characters for password.');
            setLoading(false);
            return;
        }

        try {
            const currentUser = await customerRegister(
                firstName.trim(),
                lastName.trim(),
                email.trim(),
                password,
                phoneNumber.trim(),
            );
            if (currentUser) {
                resetAndNavigate('ProductDashboard');
            }
        } catch (error: any) {
            const message = error?.response?.data?.message || 'Registration failed. Please try again.';
            Alert.alert('Signup failed', message);
        } finally {
            setLoading(false);
        }
    };

    const handleGesture = ({ nativeEvent }: any) => {
        if (nativeEvent.state === State.END) {
            const { translationX, translationY } = nativeEvent;
            let direction = '';
            if (Math.abs(translationX) > Math.abs(translationY)) {
                direction = translationX > 0 ? 'right' : 'left';
            } else {
                direction = translationY > 0 ? 'down' : 'up';
            }

            if (direction === 'up' && nativeEvent.translationY < -100) {
                navigate('CustomerLogin');
            }
        }
    };

    return (
        <GestureHandlerRootView style={styles.container}>
            <CustomSafeAreaView>
                <ProductSlider />
                <PanGestureHandler onHandlerStateChange={handleGesture}>
                    <Animated.ScrollView
                        bounces={false}
                        keyboardDismissMode="on-drag"
                        keyboardShouldPersistTaps="handled"
                        contentContainerStyle={styles.subContainer}
                        style={{ transform: [{ translateY: animatedValue }] }}
                    >
                        <LinearGradient colors={bottomColors} style={styles.gradient} />
                        <View style={styles.content}>
                            <Image source={require('@assets/images/logo.png')} style={styles.logo} />
                            <CustomText variant="h2" fontFamily={Fonts.Bold}>
                                Create an account
                            </CustomText>
                            <CustomText variant="h5" fontFamily={Fonts.SemiBold} style={styles.text}>
                                Sign up to start shopping
                            </CustomText>
                            <CustomInput onChangeText={setFirstName} value={firstName} placeholder="First name" />
                            <CustomInput onChangeText={setLastName} value={lastName} placeholder="Last name" />
                            <CustomInput onChangeText={setEmail} value={email} placeholder="Email" inputMode="email" />
                            <CustomInput onChangeText={setPassword} value={password} placeholder="Password" secureTextEntry />
                            <CustomInput onChangeText={setPhoneNumber} value={phoneNumber} placeholder="Phone number" inputMode="tel" />
                            <Button loading={loading} onPress={handleRegister} disabled={loading} title="Sign up" />
                            <Pressable onPress={() => navigate('CustomerLogin')} style={styles.linkContainer}>
                                <CustomText fontFamily={Fonts.Medium} style={styles.linkText}>
                                    Already have an account? Log in
                                </CustomText>
                            </Pressable>
                        </View>
                    </Animated.ScrollView>
                </PanGestureHandler>
            </CustomSafeAreaView>
            <View style={styles.footer}>
                <SafeAreaView>
                    <CustomText fontSize={RFValue(6)}>By continuing, you agree to our T&C</CustomText>
                </SafeAreaView>
            </View>
        </GestureHandlerRootView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    text: {
        marginTop: 2,
        marginBottom: 25,
        opacity: 0.8,
    },
    subContainer: {
        flex: 1,
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginBottom: 20,
    },
    footer: {
        borderTopWidth: 0.8,
        borderColor: Colors.border,
        paddingBottom: 10,
        zIndex: 22,
        position: 'absolute',
        bottom: 0,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 10,
        backgroundColor: '#f8f9fc',
        width: '100%',
    },
    gradient: {
        paddingTop: 60,
        width: '100%',
    },
    content: {
        justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
        backgroundColor: 'white',
        paddingHorizontal: 20,
        paddingBottom: 20,
    },
    logo: {
        height: 50,
        width: 50,
        borderRadius: 20,
        marginVertical: 10,
    },
    linkContainer: {
        marginTop: 16,
    },
    linkText: {
        color: Colors.primary,
    },
});

export default CustomerSignup;
