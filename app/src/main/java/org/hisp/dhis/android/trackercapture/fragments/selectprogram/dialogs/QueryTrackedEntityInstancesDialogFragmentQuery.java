/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.trackercapture.fragments.selectprogram.dialogs;

import android.content.Context;
import android.util.Log;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.OptionSet;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowFactory;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.AutoCompleteRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.CheckBoxRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.DatePickerRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.EditTextRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.RadioButtonsRow;
import org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.hisp.dhis.android.sdk.utils.api.ValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simen S. Russnes on 7/9/15.
 */
public class QueryTrackedEntityInstancesDialogFragmentQuery implements Query<QueryTrackedEntityInstancesDialogFragmentForm>
{
    public static final String TAG = QueryTrackedEntityInstancesDialogFragmentQuery.class.getSimpleName();
    private String orgUnit;
    private String programId;

    public QueryTrackedEntityInstancesDialogFragmentQuery(String orgUnit, String programId)
    {
        this.programId = programId;
        this.orgUnit = orgUnit;
    }

    @Override
    public QueryTrackedEntityInstancesDialogFragmentForm query(Context context)
    {
        QueryTrackedEntityInstancesDialogFragmentForm form = new QueryTrackedEntityInstancesDialogFragmentForm();
        form.setOrganisationUnit(orgUnit);
        form.setProgram(programId);

        Log.d(TAG, orgUnit + programId);

        Program program = MetaDataController.getProgram(programId);
        if(program == null || orgUnit == null) {
            return form;
        }
        List<ProgramTrackedEntityAttribute> programAttrs = program.getProgramTrackedEntityAttributes();
        List<TrackedEntityAttributeValue> values = new ArrayList<>();
        List<Row> dataEntryRows = new ArrayList<>();
        for(ProgramTrackedEntityAttribute ptea: programAttrs) {
            TrackedEntityAttribute trackedEntityAttribute = ptea.getTrackedEntityAttribute();
            TrackedEntityAttributeValue value = new TrackedEntityAttributeValue();
            value.setTrackedEntityAttributeId(trackedEntityAttribute.getUid());
            values.add(value);

            Row row = DataEntryRowFactory.createDataEntryView(ptea.getMandatory(),
                    ptea.getAllowFutureDate(), trackedEntityAttribute.getOptionSet(),
                    trackedEntityAttribute.getName(), value, trackedEntityAttribute.getValueType(), true, false);
            dataEntryRows.add(row);
        }
        form.setTrackedEntityAttributeValues(values);
        form.setDataEntryRows(dataEntryRows);
        return form;
    }
}
