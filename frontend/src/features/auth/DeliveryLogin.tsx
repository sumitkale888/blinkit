import { Alert, ScrollView, StyleSheet, View } from 'react-native';
import React, { FC, useState } from 'react';
import { resetAndNavigate } from '@utils/NavigationUtils';
import { deliveryLogin } from '@service/authService';
import CustomSafeAreaView from '@components/global/CustomSafeAreaView';
import { screenHeight } from '@utils/Scaling';
import LottieView from 'lottie-react-native';
import CustomText from '@components/ui/CustomText';
import { Fonts } from '@utils/Constants';
import CustomInput from '@components/ui/CustomInput';
import { RFValue } from 'react-native-responsive-fontsize';
import Button from '@components/ui/Button';

const DeliveryLogin: FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    setLoading(true);
    try {
      const currentUser = await deliveryLogin(email.trim(), password);
      if (currentUser?.role === 'ROLE_ADMIN') {
        resetAndNavigate('DeliveryDashboard');
      } else {
        resetAndNavigate('CustomerLogin');
      }
    } catch (error) {
      Alert.alert('Login Failed', 'Invalid credentials or access denied.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <CustomSafeAreaView>
      <ScrollView keyboardShouldPersistTaps="handled" keyboardDismissMode="on-drag">
        <View style={styles.container}>
          <View style={styles.lottieContainer}>
            <LottieView autoPlay loop style={styles.lottie} source={require('@assets/animations/delivery_man.json')} />
          </View>

          <CustomText variant="h3" fontFamily={Fonts.Bold}>
            Delivery Partner Portal
          </CustomText>

          <CustomText variant="h6" fontFamily={Fonts.SemiBold}>
            Faster than Flash⚡️
          </CustomText>

          <CustomInput
            onChangeText={setEmail}
            value={email}
            left={
              <CustomText variant="h6" fontFamily={Fonts.SemiBold} style={styles.inputIcon}>
                📧
              </CustomText>
            }
            placeholder="Email"
            inputMode="email"
          />

          <CustomInput
            onChangeText={setPassword}
            value={password}
            left={
              <CustomText variant="h6" fontFamily={Fonts.SemiBold} style={styles.inputIcon}>
                🔒
              </CustomText>
            }
            placeholder="Password"
            secureTextEntry
          />

          <Button
            disabled={!email.trim() || password.length < 6}
            title="Login"
            onPress={handleLogin}
            loading={loading}
          />
        </View>
      </ScrollView>
    </CustomSafeAreaView>
  );
};

export default DeliveryLogin;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    alignItems: 'center',
  },
  lottie: {
    height: '100%',
    width: '100%',
  },
  lottieContainer: {
    height: screenHeight * 0.12,
    width: '100%',
  },
  inputIcon: {
    marginLeft: 10,
  },
});
