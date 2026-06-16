import React from 'react';
import { ActivityIndicator, StyleSheet, View } from 'react-native';
import { Colors, Fonts } from '@utils/Constants';
import CustomText from './CustomText';

type Props =
  | { status: 'loading'; text?: string }
  | { status: 'empty'; text?: string }
  | { status: 'error'; text: string };

const ContentState = (props: Props) => {
  if (props.status === 'loading') {
    return (
      <View style={styles.container}>
        <ActivityIndicator size="large" color={Colors.primary} />
        {!!props.text && (
          <CustomText variant="h6" style={styles.text} fontFamily={Fonts.Medium}>
            {props.text}
          </CustomText>
        )}
      </View>
    );
  }

  if (props.status === 'empty') {
    return (
      <View style={styles.container}>
        <CustomText variant="h6" style={styles.text} fontFamily={Fonts.Medium}>
          {props.text || 'No items found'}
        </CustomText>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <CustomText variant="h6" style={styles.text} fontFamily={Fonts.Medium}>
        {props.text}
      </CustomText>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 16 },
  text: { color: Colors.text, textAlign: 'center', marginTop: 8 },
});

export default ContentState;
