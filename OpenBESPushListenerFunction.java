/*
 * Copyright 2010-2011 Research In Motion Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blackberry.push;

import net.rim.device.api.script.Scriptable;
import net.rim.device.api.script.ScriptableFunction;
import blackberry.core.FunctionSignature;
import blackberry.core.ScriptableFunctionBase;

/**
 * Class to implement the script function OpenBESPushListenerFunction()
 */
public class OpenBESPushListenerFunction extends ScriptableFunctionBase {

    private static final String KEY_PORT = "port";
    private static final String KEY_WAKEUP_PAGE = "wakeUpPage";
    private static final String KEY_MAX_QUEUE_CAP = "maxQueueCap";

    /**
     * @see ScriptableFunctionBase#execute(Object, Object[])
     */
    protected Object execute( Object thiz, Object[] args ) throws Exception {
        Scriptable obj = (Scriptable) args[ 0 ];
        int port = ( (Integer) obj.getField( KEY_PORT ) ).intValue();
        String wakeUpPage = obj.getField( KEY_WAKEUP_PAGE ).toString();
        int maxQueueCap = 0;
        Object maxQueueCapObj = obj.getField( KEY_MAX_QUEUE_CAP );
        if( maxQueueCapObj != UNDEFINED ) {
            maxQueueCap = ( (Integer) maxQueueCapObj ).intValue(); 
        }
        ScriptableFunction onData = (ScriptableFunction) args[ 1 ];
        ScriptableFunction onSimChange = (ScriptableFunction) args[ 2 ];
        PushService.getInstance().openBESPushChannel( port, wakeUpPage, maxQueueCap, onData, onSimChange );
        return UNDEFINED;
    }

    /**
     * @see ScriptableFunctionBase#validateArg(Object[])
     */
    protected void validateArg( Object[] args ) {
        super.validateArg( args );

        Scriptable obj = (Scriptable) args[ 0 ];
        try {
            Object port = obj.getField( KEY_PORT );
            if( port != null && port != UNDEFINED ) {
                int portValue = ( (Integer) port ).intValue();
                if( portValue < 0 ) {
                    throw new IllegalArgumentException( "Invalid port." );
                } else if( !PushService.isValidPort( portValue ) ) {
                    throw new IllegalArgumentException( "Reserved port" );
                }
            } else {
                throw new IllegalArgumentException( "Port is missing." );
            }
            Object wakeUpPage = obj.getField( KEY_WAKEUP_PAGE );
            if( wakeUpPage == null || wakeUpPage == UNDEFINED ) {
                throw new IllegalArgumentException( "AppId is missing." );
            }
        } catch( Exception e ) {
            throw new IllegalArgumentException( "Error retrieving arguments: " + e.getMessage() );
        }
    }

    /**
     * @see ScriptableFunctionBase#getFunctionSignatures()
     */
    protected FunctionSignature[] getFunctionSignatures() {
        FunctionSignature fs = new FunctionSignature( 3 );
        fs.addParam( Object.class, true );
        fs.addParam( ScriptableFunction.class, true );
        fs.addParam( ScriptableFunction.class, true );
        return new FunctionSignature[] { fs };
    }

}
