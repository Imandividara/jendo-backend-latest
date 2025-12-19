import React, { useState, useRef, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, Alert } from 'react-native';
import { useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { ScreenWrapper } from '../../../common/components/layout';
import { Button } from '../../../common/components/ui';
import { COLORS } from '../../../config/theme.config';
import { authStyles as styles } from '../components';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { authApi } from '../services/authApi';
import { useAuth } from '../../../providers/AuthProvider';


export const VerifyOTPScreen: React.FC = () => {
  const router = useRouter();
  const { signup } = useAuth();
  const [otp, setOtp] = useState(['', '', '', '', '', '']);
  const [loading, setLoading] = useState(false);
  const [timer, setTimer] = useState(59);
  const inputRefs = useRef<(TextInput | null)[]>([]);

  useEffect(() => {
    if (timer > 0) {
      const interval = setInterval(() => {
        setTimer(prev => prev - 1);
      }, 1000);
      return () => clearInterval(interval);
    }
  }, [timer]);

  const handleOtpChange = (value: string, index: number) => {
    if (value.length > 1) return;
    
    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);

    if (value && index < 5) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handleKeyPress = (key: string, index: number) => {
    if (key === 'Backspace' && !otp[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  const handleVerify = async () => {
    setLoading(true);
    try {
      const otpCode = otp.join('');
      // Get signup data from AsyncStorage
      const signupDataStr = await AsyncStorage.getItem('signupData');
      if (!signupDataStr) throw new Error('Signup data not found');
      const signupData = JSON.parse(signupDataStr);
      const email = signupData.email;

      // 1. Verify OTP
      await authApi.verifyOtp({ email, otp: otpCode });
      // 2. Send signup data to backend using useAuth hook (stores user in Zustand)
      await signup(signupData);
      // 3. Clear AsyncStorage
      await AsyncStorage.removeItem('signupData');
      setLoading(false);
      
      router.replace('/(tabs)');
    } catch (err) {
      setLoading(false);
      const errorMessage = err instanceof Error ? err.message : 'Verification failed';
      Alert.alert('Error', errorMessage);
    }
  };

  const handleResend = () => {
    setOtp(['', '', '', '', '', '']);
    setTimer(59);
  };

  const isComplete = otp.every(digit => digit !== '');
  const formattedTime = `0:${timer.toString().padStart(2, '0')}`;

  return (
    <ScreenWrapper safeArea backgroundColor={COLORS.white}>
      <View style={styles.container}>
        <View style={styles.header}>
          <TouchableOpacity style={styles.backButton} onPress={() => router.back()}>
            <Ionicons name="arrow-back" size={24} color={COLORS.textPrimary} />
          </TouchableOpacity>
          <Text style={styles.logo}>JENDO</Text>
        </View>

        <View style={styles.contentTop}>
          <View style={styles.iconContainerSmall}>
            <View style={styles.iconCircleSmall}>
              <Ionicons name="mail-outline" size={40} color={COLORS.primary} />
            </View>
          </View>

          <Text style={styles.title}>Verify Code</Text>
          <Text style={styles.subtitle}>
            We've sent a verification code to your email. Please enter the code below.
          </Text>

          <View style={styles.otpContainer}>
            {otp.map((digit, index) => (
              <TextInput
                key={index}
                ref={(ref) => { inputRefs.current[index] = ref; }}
                style={[styles.otpInput, digit && styles.otpInputFilled]}
                value={digit}
                onChangeText={(value) => handleOtpChange(value, index)}
                onKeyPress={({ nativeEvent }) => handleKeyPress(nativeEvent.key, index)}
                keyboardType="number-pad"
                maxLength={1}
                selectTextOnFocus
              />
            ))}
          </View>

          {timer > 0 ? (
            <Text style={styles.timerText}>Resend code in {formattedTime}</Text>
          ) : (
            <TouchableOpacity onPress={handleResend}>
              <Text style={styles.resendLink}>Resend Code</Text>
            </TouchableOpacity>
          )}

          <Button
            title="Verify Code"
            onPress={handleVerify}
            loading={loading}
            disabled={!isComplete}
            style={styles.verifyButton}
          />
        </View>
      </View>
    </ScreenWrapper>
  );
};
